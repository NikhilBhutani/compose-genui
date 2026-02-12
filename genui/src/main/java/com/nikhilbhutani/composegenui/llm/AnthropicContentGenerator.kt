package com.nikhilbhutani.composegenui.llm

import com.nikhilbhutani.composegenui.GenUiMessage
import com.nikhilbhutani.composegenui.GenUiMessageRole
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * [GenUiContentGenerator] implementation for the Anthropic Messages API.
 */
class AnthropicContentGenerator(
    private val apiKey: String,
    private val model: String = "claude-sonnet-4-5-20250929",
    private val baseUrl: String = "https://api.anthropic.com"
) : GenUiLlmContentGenerator() {

    override suspend fun callLlm(
        systemPrompt: String,
        history: List<GenUiMessage>,
        userMessage: String,
        maxTokens: Int,
        temperature: Float
    ): String {
        val messages = mutableListOf<AnthropicMsg>()
        for (msg in history) {
            val role = when (msg.role) {
                GenUiMessageRole.USER, GenUiMessageRole.SYSTEM -> "user"
                GenUiMessageRole.ASSISTANT -> "assistant"
            }
            messages.add(AnthropicMsg(role = role, content = msg.content))
        }
        messages.add(AnthropicMsg(role = "user", content = userMessage))

        val apiRequest = AnthropicApiRequest(
            model = model,
            max_tokens = maxTokens,
            system = systemPrompt,
            messages = messages
        )

        val requestBody = json.encodeToString(apiRequest)
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$baseUrl/v1/messages")
            .addHeader("Content-Type", "application/json")
            .addHeader("x-api-key", apiKey)
            .addHeader("anthropic-version", "2023-06-01")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        if (!response.isSuccessful) {
            throw LlmApiException(
                provider = LlmProvider.ANTHROPIC,
                statusCode = response.code,
                message = "Anthropic API error: $responseBody"
            )
        }

        val apiResponse = json.decodeFromString<AnthropicApiResponse>(responseBody)
        return apiResponse.content.firstOrNull()?.text
            ?: throw LlmApiException(
                provider = LlmProvider.ANTHROPIC,
                statusCode = response.code,
                message = "Empty response from Anthropic"
            )
    }
}

@Serializable
private data class AnthropicApiRequest(
    val model: String,
    val max_tokens: Int,
    val system: String? = null,
    val messages: List<AnthropicMsg>
)

@Serializable
private data class AnthropicMsg(
    val role: String,
    val content: String
)

@Serializable
private data class AnthropicApiResponse(
    val id: String,
    val type: String,
    val role: String,
    val content: List<AnthropicContentBlock>,
    val model: String,
    val stop_reason: String? = null
)

@Serializable
private data class AnthropicContentBlock(
    val type: String,
    val text: String? = null
)
