package com.nikhilbhutani.composegenui.llm

import com.nikhilbhutani.composegenui.GenUiContentGenerator
import com.nikhilbhutani.composegenui.GenUiMessage
import com.nikhilbhutani.composegenui.GenUiMessageRole
import com.nikhilbhutani.composegenui.GenUiRequest
import com.nikhilbhutani.composegenui.GenUiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Base class for LLM-backed content generators. Wraps the HTTP call in a [Flow]
 * on [Dispatchers.IO]. Subclasses implement [callLlm] with provider-specific API logic.
 *
 * No local conversation history (managed by [GenUiConversation]),
 * receives system prompt from the request, and returns a [Flow].
 */
abstract class GenUiLlmContentGenerator : GenUiContentGenerator {

    protected val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    protected val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    override fun generate(request: GenUiRequest): Flow<GenUiResponse> = flow {
        try {
            val response = callLlm(
                systemPrompt = request.systemPrompt,
                history = request.history,
                userMessage = request.userMessage,
                maxTokens = request.maxTokens,
                temperature = request.temperature
            )
            val jsonStr = extractJson(response)
            emit(GenUiResponse.UiDocument(jsonStr))
            emit(GenUiResponse.Done)
        } catch (e: Exception) {
            emit(GenUiResponse.Error(e.message ?: "LLM call failed", e))
            emit(GenUiResponse.Done)
        }
    }.flowOn(Dispatchers.IO)

    override fun clearHistory() {
        // No local history — managed by GenUiConversation
    }

    /**
     * Subclasses implement this to call the LLM API and return the raw response text.
     */
    protected abstract suspend fun callLlm(
        systemPrompt: String,
        history: List<GenUiMessage>,
        userMessage: String,
        maxTokens: Int,
        temperature: Float
    ): String

    private fun extractJson(response: String): String {
        val trimmed = response.trim()
        val jsonBlockRegex = Regex("```(?:json)?\\s*\\n?([\\s\\S]*?)\\n?```")
        val match = jsonBlockRegex.find(trimmed)
        if (match != null) {
            return match.groupValues[1].trim()
        }
        val startIndex = trimmed.indexOf('{')
        val endIndex = trimmed.lastIndexOf('}')
        if (startIndex != -1 && endIndex > startIndex) {
            return trimmed.substring(startIndex, endIndex + 1)
        }
        return trimmed
    }
}
