package com.example.genui.llm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * A2UI content generator using OpenAI's Chat Completions API.
 *
 * Supports GPT-4, GPT-4 Turbo, GPT-3.5 Turbo, and other OpenAI chat models.
 *
 * @param config LLM configuration with OPENAI provider
 */
class OpenAiGenerator(config: A2UiLlmConfig) : A2UiLlmGenerator(config) {

    init {
        require(config.provider == LlmProvider.OPENAI) {
            "OpenAiGenerator requires OPENAI provider"
        }
    }

    override suspend fun callLlm(userMessage: String): String = withContext(Dispatchers.IO) {
        val url = URL("${config.effectiveBaseUrl()}/v1/chat/completions")
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer ${config.apiKey}")
            connection.doOutput = true

            val request = OpenAiRequest(
                model = config.effectiveModel(),
                max_tokens = config.maxTokens,
                temperature = config.temperature,
                messages = listOf(
                    OpenAiMessage(role = "system", content = systemPrompt),
                    OpenAiMessage(role = "user", content = userMessage)
                ),
                response_format = OpenAiResponseFormat(type = "json_object")
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
                    provider = LlmProvider.OPENAI,
                    statusCode = responseCode,
                    message = "OpenAI API error: $errorBody"
                )
            }

            val responseBody = connection.inputStream.bufferedReader().readText()
            val response = json.decodeFromString<OpenAiResponse>(responseBody)

            response.choices.firstOrNull()?.message?.content
                ?: throw LlmApiException(
                    provider = LlmProvider.OPENAI,
                    statusCode = responseCode,
                    message = "Empty response from OpenAI"
                )
        } finally {
            connection.disconnect()
        }
    }
}

@Serializable
private data class OpenAiRequest(
    val model: String,
    val max_tokens: Int,
    val temperature: Float,
    val messages: List<OpenAiMessage>,
    val response_format: OpenAiResponseFormat? = null
)

@Serializable
private data class OpenAiMessage(
    val role: String,
    val content: String
)

@Serializable
private data class OpenAiResponseFormat(
    val type: String
)

@Serializable
private data class OpenAiResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<OpenAiChoice>
)

@Serializable
private data class OpenAiChoice(
    val index: Int,
    val message: OpenAiMessage,
    val finish_reason: String? = null
)
