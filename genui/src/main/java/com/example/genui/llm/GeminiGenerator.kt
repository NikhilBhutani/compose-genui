package com.example.genui.llm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * A2UI content generator using Google's Gemini API.
 *
 * Supports Gemini Pro, Gemini Ultra, and other Gemini models.
 *
 * @param config LLM configuration with GEMINI provider
 */
class GeminiGenerator(config: A2UiLlmConfig) : A2UiLlmGenerator(config) {

    init {
        require(config.provider == LlmProvider.GEMINI) {
            "GeminiGenerator requires GEMINI provider"
        }
    }

    override suspend fun callLlm(userMessage: String): String = withContext(Dispatchers.IO) {
        val model = config.effectiveModel()
        val url = URL(
            "${config.effectiveBaseUrl()}/v1beta/models/$model:generateContent?key=${config.apiKey}"
        )
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val request = GeminiRequest(
                contents = listOf(
                    GeminiContent(
                        parts = listOf(GeminiPart(text = userMessage))
                    )
                ),
                systemInstruction = GeminiContent(
                    parts = listOf(GeminiPart(text = systemPrompt))
                ),
                generationConfig = GeminiGenerationConfig(
                    maxOutputTokens = config.maxTokens,
                    temperature = config.temperature,
                    responseMimeType = "application/json"
                )
            )

            val requestBody = json.encodeToString(request)

            OutputStreamWriter(connection.outputStream).use { writer ->
                writer.write(requestBody)
                writer.flush()
            }

            val responseCode = connection.responseCode
            if (responseCode != 200) {
                val errorBody = connection.errorStream?.bufferedReader()?.readText()
                throw LlmApiException(
                    provider = LlmProvider.GEMINI,
                    statusCode = responseCode,
                    message = "Gemini API error: $errorBody"
                )
            }

            val responseBody = connection.inputStream.bufferedReader().readText()
            val response = json.decodeFromString<GeminiResponse>(responseBody)

            response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text
                ?: throw LlmApiException(
                    provider = LlmProvider.GEMINI,
                    statusCode = responseCode,
                    message = "Empty response from Gemini"
                )
        } finally {
            connection.disconnect()
        }
    }
}

@Serializable
private data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null,
    val generationConfig: GeminiGenerationConfig? = null
)

@Serializable
private data class GeminiContent(
    val parts: List<GeminiPart>,
    val role: String? = null
)

@Serializable
private data class GeminiPart(
    val text: String
)

@Serializable
private data class GeminiGenerationConfig(
    val maxOutputTokens: Int? = null,
    val temperature: Float? = null,
    val responseMimeType: String? = null
)

@Serializable
private data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

@Serializable
private data class GeminiCandidate(
    val content: GeminiContent? = null,
    val finishReason: String? = null
)
