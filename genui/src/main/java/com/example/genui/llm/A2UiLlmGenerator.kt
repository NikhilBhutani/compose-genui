package com.example.genui.llm

import com.example.genui.A2UiContentGenerator
import com.example.genui.A2UiDocument
import com.example.genui.A2UiEvent
import com.example.genui.A2UiState
import com.example.genui.parseA2UiDocument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Base class for LLM-based A2UI content generators.
 * Implementations handle provider-specific API calls.
 */
abstract class A2UiLlmGenerator(
    protected val config: A2UiLlmConfig
) : A2UiContentGenerator {

    protected val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    protected val systemPrompt: String
        get() = config.systemPrompt ?: A2UiPrompts.DEFAULT_SYSTEM_PROMPT

    override suspend fun generate(
        document: A2UiDocument,
        state: A2UiState,
        event: A2UiEvent?
    ): A2UiDocument {
        val currentDocJson = json.encodeToString(document)
        val stateMap = state.values.mapValues { it.value.toString() }
        val eventStr = event?.let { "${it.nodeId}:${it.action}" }

        val userMessage = A2UiPrompts.buildUserMessage(
            currentDocument = currentDocJson,
            state = stateMap,
            event = eventStr,
            userIntent = null
        )

        val response = callLlm(userMessage)
        return parseResponse(response)
    }

    /**
     * Generate UI from a user intent/prompt without an existing document.
     */
    suspend fun generateFromPrompt(prompt: String): A2UiDocument {
        val userMessage = "Generate an A2UI interface for: $prompt\n\nRespond with valid A2UI JSON only."
        val response = callLlm(userMessage)
        return parseResponse(response)
    }

    /**
     * Make the actual API call to the LLM provider.
     * Implementations must override this.
     */
    protected abstract suspend fun callLlm(userMessage: String): String

    /**
     * Parse the LLM response into an A2UiDocument.
     * Handles JSON extraction from markdown code blocks if present.
     */
    protected fun parseResponse(response: String): A2UiDocument {
        val jsonStr = extractJson(response)
        return parseA2UiDocument(jsonStr)
    }

    /**
     * Extract JSON from response, handling markdown code blocks.
     */
    private fun extractJson(response: String): String {
        val trimmed = response.trim()
        
        // Handle ```json ... ``` blocks
        val jsonBlockRegex = Regex("```(?:json)?\\s*\\n?([\\s\\S]*?)\\n?```")
        val match = jsonBlockRegex.find(trimmed)
        if (match != null) {
            return match.groupValues[1].trim()
        }
        
        // Try to find JSON object directly
        val startIndex = trimmed.indexOf('{')
        val endIndex = trimmed.lastIndexOf('}')
        if (startIndex != -1 && endIndex > startIndex) {
            return trimmed.substring(startIndex, endIndex + 1)
        }
        
        return trimmed
    }
}

/**
 * Result wrapper for LLM generation with error handling.
 */
sealed class A2UiGenerationResult {
    data class Success(val document: A2UiDocument) : A2UiGenerationResult()
    data class Error(val message: String, val cause: Throwable? = null) : A2UiGenerationResult()
}

/**
 * Extension to generate with error handling.
 */
suspend fun A2UiLlmGenerator.generateSafe(
    document: A2UiDocument,
    state: A2UiState,
    event: A2UiEvent?
): A2UiGenerationResult {
    return try {
        A2UiGenerationResult.Success(generate(document, state, event))
    } catch (e: Exception) {
        A2UiGenerationResult.Error(e.message ?: "Unknown error", e)
    }
}
