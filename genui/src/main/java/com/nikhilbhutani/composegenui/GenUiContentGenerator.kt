package com.nikhilbhutani.composegenui

import kotlinx.coroutines.flow.Flow

/**
 * Pluggable interface for content generation backends (LLM providers, mock generators, etc.).
 * Returns a [Flow] of [GenUiResponse] to support streaming.
 */
interface GenUiContentGenerator {
    fun generate(request: GenUiRequest): Flow<GenUiResponse>
    fun clearHistory()
}

/**
 * A structured request sent to the content generator. The conversation layer
 * builds this with the system prompt (including catalog schema) and conversation history.
 */
data class GenUiRequest(
    val systemPrompt: String,
    val history: List<GenUiMessage>,
    val userMessage: String,
    val maxTokens: Int = 4096,
    val temperature: Float = 0.7f
)

data class GenUiMessage(
    val role: GenUiMessageRole,
    val content: String
)

enum class GenUiMessageRole {
    USER, ASSISTANT, SYSTEM
}

/**
 * Responses emitted by a [GenUiContentGenerator].
 */
sealed class GenUiResponse {
    /** Non-UI text from the LLM (e.g. explanation, commentary). */
    data class Text(val content: String) : GenUiResponse()

    /** A complete A2UI JSON document. */
    data class UiDocument(val json: String) : GenUiResponse()

    /** A streaming chunk of A2UI JSON (for future streaming support). */
    data class UiDocumentChunk(val partial: String) : GenUiResponse()

    /** An error occurred during generation. */
    data class Error(val message: String, val cause: Throwable? = null) : GenUiResponse()

    /** Generation is complete. */
    data object Done : GenUiResponse()
}

/**
 * Structured error information passed to the [GenUiConversation] error callback.
 */
data class GenUiError(
    val message: String,
    val cause: Throwable? = null
)
