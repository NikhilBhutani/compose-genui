package com.nikhilbhutani.composegenui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement

/**
 * The orchestration facade for GenUI. Manages the agentic loop:
 *
 * 1. Developer calls [sendRequest] with a user prompt
 * 2. Conversation builds a system prompt with catalog schema + user message with context
 * 3. Generator produces responses (UI documents, text, errors)
 * 4. UI updates automatically via [document] StateFlow
 * 5. User interactions are captured and fed back into the next request
 */
class GenUiConversation(
    private val catalog: GenUiCatalog,
    private val contentGenerator: GenUiContentGenerator,
    private val onTextResponse: ((String) -> Unit)? = null,
    private val onError: ((GenUiError) -> Unit)? = null,
    private val autoFollowUp: Boolean = false,
    private val systemPromptPrefix: String? = null,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {
    private val _document = MutableStateFlow<A2UiDocument?>(null)
    val document: StateFlow<A2UiDocument?> = _document.asStateFlow()

    private val _uiState = MutableStateFlow(A2UiState())
    val uiState: StateFlow<A2UiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /** The A2UiCatalog derived from the GenUiCatalog, for use with A2UiSurface. */
    val renderCatalog: A2UiCatalog = catalog.asA2UiCatalog()

    private val conversationHistory = mutableListOf<GenUiMessage>()
    private var lastUserAction: A2UiUserAction? = null

    private val fullSystemPrompt: String by lazy {
        buildString {
            appendLine(systemPromptPrefix ?: DEFAULT_SYSTEM_PREFIX)
            appendLine()
            appendLine("=== AVAILABLE COMPONENT CATALOG ===")
            append(catalog.toSchemaDescription())
            appendLine("=== END CATALOG ===")
            appendLine()
            appendLine(RESPONSE_FORMAT_INSTRUCTIONS)
        }
    }

    /**
     * Main entry point. Sends a user prompt to the content generator asynchronously.
     * Builds the user message with current document, state, and last action context.
     */
    fun sendRequest(prompt: String) {
        scope.launch {
            sendRequestSuspend(prompt)
        }
    }

    /**
     * Suspending version of [sendRequest] for callers that want to await completion.
     */
    suspend fun sendRequestSuspend(prompt: String) {
        _isLoading.value = true

        val userMessage = buildUserMessage(prompt)
        conversationHistory.add(GenUiMessage(GenUiMessageRole.USER, userMessage))

        val request = GenUiRequest(
            systemPrompt = fullSystemPrompt,
            history = conversationHistory.toList(),
            userMessage = userMessage
        )

        try {
            contentGenerator.generate(request).collect { response ->
                when (response) {
                    is GenUiResponse.UiDocument -> {
                        val doc = parseA2UiDocument(response.json)
                        _document.value = doc
                        conversationHistory.add(
                            GenUiMessage(GenUiMessageRole.ASSISTANT, response.json)
                        )
                    }
                    is GenUiResponse.Text -> {
                        onTextResponse?.invoke(response.content)
                        conversationHistory.add(
                            GenUiMessage(GenUiMessageRole.ASSISTANT, response.content)
                        )
                    }
                    is GenUiResponse.Error -> {
                        onError?.invoke(GenUiError(response.message, response.cause))
                    }
                    is GenUiResponse.UiDocumentChunk -> {
                        // Future streaming support
                    }
                    is GenUiResponse.Done -> {
                        // Generation complete
                    }
                }
            }
        } catch (e: Exception) {
            onError?.invoke(GenUiError(e.message ?: "Unknown error", e))
        } finally {
            _isLoading.value = false
            lastUserAction = null
        }
    }

    /**
     * Called by [GenUiSurface] when the user performs an action (button click, etc.).
     * Updates state, stores action context for the next prompt, and optionally auto-follows-up.
     */
    fun onUserAction(action: A2UiUserAction) {
        lastUserAction = action
        if (autoFollowUp) {
            sendRequest("User performed action: ${action.actionName} on ${action.sourceComponentId}")
        }
    }

    /**
     * Lower-level event handler for state updates (e.g., text input changes).
     */
    fun onEvent(event: A2UiEvent) {
        if (event.action == "input") {
            val value = event.payload["value"] ?: return
            _uiState.update { it.withValue(event.nodeId, value) }
        }
    }

    /** Clears conversation history, state, document, and generator history. */
    fun reset() {
        conversationHistory.clear()
        lastUserAction = null
        _document.value = null
        _uiState.value = A2UiState()
        _isLoading.value = false
        contentGenerator.clearHistory()
    }

    private fun buildUserMessage(prompt: String): String = buildString {
        appendLine("User request: $prompt")

        val currentDoc = _document.value
        if (currentDoc != null) {
            appendLine("Current UI document: ${A2UiJson.encodeDocument(currentDoc)}")
        }

        val state = _uiState.value
        if (state.values.isNotEmpty()) {
            val stateStr = state.values.entries.joinToString(", ") { "${it.key}=${it.value}" }
            appendLine("Current form state: $stateStr")
        }

        val action = lastUserAction
        if (action != null) {
            appendLine("Last user action: ${action.actionName} on ${action.sourceComponentId}")
        }

        appendLine("Generate the A2UI JSON document.")
    }

    companion object {
        private val DEFAULT_SYSTEM_PREFIX = """
            You are an A2UI (Agent-to-User Interface) generator. You generate JSON documents
            that describe user interfaces using the component catalog below.

            The A2UI format is a JSON structure with a root node containing nested components.
            Each component has:
            - type: Component type from the catalog
            - id: Optional unique identifier for interactive components
            - props: Component properties (varies by type)
            - children: Nested child components (for containers)
        """.trimIndent()

        private val RESPONSE_FORMAT_INSTRUCTIONS = """
            Response format:
            - Respond ONLY with a valid A2UI JSON document
            - The JSON must have a "root" object with at least a "type" field
            - Use component types and props from the catalog above
            - Give interactive components unique "id" values
            - Do not include markdown fences or explanation text

            Example:
            {"root":{"type":"column","props":{"padding":16,"spacing":8},"children":[{"type":"text","props":{"text":"Hello!","fontSize":24,"fontWeight":"bold"}},{"type":"button","id":"greet","props":{"label":"Click me"}}]}}
        """.trimIndent()
    }
}
