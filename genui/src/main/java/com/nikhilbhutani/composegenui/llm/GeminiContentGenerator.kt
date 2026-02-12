package com.nikhilbhutani.composegenui.llm

import com.nikhilbhutani.composegenui.GenUiMessage
import com.nikhilbhutani.composegenui.GenUiMessageRole
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * [GenUiContentGenerator] implementation for Google's Gemini API.
 */
class GeminiContentGenerator(
    private val apiKey: String,
    private val model: String = "gemini-2.0-flash",
    private val baseUrl: String = "https://generativelanguage.googleapis.com"
) : GenUiLlmContentGenerator() {

    override suspend fun callLlm(
        systemPrompt: String,
        history: List<GenUiMessage>,
        userMessage: String,
        maxTokens: Int,
        temperature: Float
    ): String {
        val contents = mutableListOf<GeminiContentBlock>()
        for (msg in history) {
            val role = when (msg.role) {
                GenUiMessageRole.ASSISTANT -> "model"
                GenUiMessageRole.USER -> "user"
                GenUiMessageRole.SYSTEM -> "user" // Gemini doesn't have a system role in contents
            }
            contents.add(GeminiContentBlock(parts = listOf(GeminiPart(text = msg.content)), role = role))
        }
        contents.add(GeminiContentBlock(parts = listOf(GeminiPart(text = userMessage)), role = "user"))

        val apiRequest = GeminiApiRequest(
            contents = contents,
            systemInstruction = GeminiContentBlock(parts = listOf(GeminiPart(text = systemPrompt))),
            generationConfig = GeminiGenConfig(
                maxOutputTokens = maxTokens,
                temperature = temperature,
                responseMimeType = "application/json"
            )
        )

        val requestBody = json.encodeToString(apiRequest)
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$baseUrl/v1beta/models/$model:generateContent?key=$apiKey")
            .addHeader("Content-Type", "application/json")
            .post(requestBody)
            .build()

        val response = httpClient.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        if (!response.isSuccessful) {
            throw LlmApiException(
                provider = LlmProvider.GEMINI,
                statusCode = response.code,
                message = "Gemini API error: $responseBody"
            )
        }

        val apiResponse = json.decodeFromString<GeminiApiResponse>(responseBody)
        return apiResponse.candidates?.firstOrNull()
            ?.content?.parts?.firstOrNull()?.text
            ?: throw LlmApiException(
                provider = LlmProvider.GEMINI,
                statusCode = response.code,
                message = "Empty response from Gemini"
            )
    }
}

@Serializable
private data class GeminiApiRequest(
    val contents: List<GeminiContentBlock>,
    val systemInstruction: GeminiContentBlock? = null,
    val generationConfig: GeminiGenConfig? = null
)

@Serializable
private data class GeminiContentBlock(
    val parts: List<GeminiPart>,
    val role: String? = null
)

@Serializable
private data class GeminiPart(
    val text: String
)

@Serializable
private data class GeminiGenConfig(
    val maxOutputTokens: Int? = null,
    val temperature: Float? = null,
    val responseMimeType: String? = null
)

@Serializable
private data class GeminiApiResponse(
    val candidates: List<GeminiApiCandidate>? = null
)

@Serializable
private data class GeminiApiCandidate(
    val content: GeminiContentBlock? = null,
    val finishReason: String? = null
)
