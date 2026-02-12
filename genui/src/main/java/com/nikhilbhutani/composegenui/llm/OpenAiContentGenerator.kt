package com.nikhilbhutani.composegenui.llm

import com.nikhilbhutani.composegenui.GenUiMessage
import com.nikhilbhutani.composegenui.GenUiMessageRole
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * [GenUiContentGenerator] implementation for OpenAI-compatible APIs.
 */
class OpenAiContentGenerator(
    private val apiKey: String,
    private val model: String = "gpt-4o",
    private val baseUrl: String = "https://api.openai.com"
) : GenUiLlmContentGenerator() {

    override suspend fun callLlm(
        systemPrompt: String,
        history: List<GenUiMessage>,
        userMessage: String,
        maxTokens: Int,
        temperature: Float
    ): String {
        val messages = mutableListOf(
            OpenAiMsg(role = "system", content = systemPrompt)
        )
        messages.addAll(history.map { msg ->
            val role = when (msg.role) {
                GenUiMessageRole.SYSTEM -> "system"
                GenUiMessageRole.USER -> "user"
                GenUiMessageRole.ASSISTANT -> "assistant"
            }
            OpenAiMsg(role = role, content = msg.content)
        })
        messages.add(OpenAiMsg(role = "user", content = userMessage))

        val apiRequest = OpenAiApiRequest(
            model = model,
            max_tokens = maxTokens,
            temperature = temperature,
            messages = messages,
            response_format = OpenAiRespFormat(type = "json_object")
        )

        val requestBody = json.encodeToString(apiRequest)
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$baseUrl/v1/chat/completions")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        if (!response.isSuccessful) {
            throw LlmApiException(
                provider = LlmProvider.OPENAI,
                statusCode = response.code,
                message = "OpenAI API error: $responseBody"
            )
        }

        val apiResponse = json.decodeFromString<OpenAiApiResponse>(responseBody)
        return apiResponse.choices.firstOrNull()?.message?.content
            ?: throw LlmApiException(
                provider = LlmProvider.OPENAI,
                statusCode = response.code,
                message = "Empty response from OpenAI"
            )
    }
}

@Serializable
private data class OpenAiApiRequest(
    val model: String,
    val max_tokens: Int,
    val temperature: Float,
    val messages: List<OpenAiMsg>,
    val response_format: OpenAiRespFormat? = null
)

@Serializable
private data class OpenAiMsg(
    val role: String,
    val content: String
)

@Serializable
private data class OpenAiRespFormat(
    val type: String
)

@Serializable
private data class OpenAiApiResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<OpenAiApiChoice>
)

@Serializable
private data class OpenAiApiChoice(
    val index: Int,
    val message: OpenAiMsg,
    val finish_reason: String? = null
)
