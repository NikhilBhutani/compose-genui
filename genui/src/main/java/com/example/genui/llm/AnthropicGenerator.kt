package com.example.genui.llm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * A2UI content generator using Anthropic's Claude API.
 *
 * Supports Claude 3.5 Sonnet, Claude 3 Opus, and other Claude models.
 *
 * @param config LLM configuration with ANTHROPIC provider
 */
class AnthropicGenerator(config: A2UiLlmConfig) : A2UiLlmGenerator(config) {

    init {
        require(config.provider == LlmProvider.ANTHROPIC) {
            "AnthropicGenerator requires ANTHROPIC provider"
        }
    }

    override suspend fun callLlm(userMessage: String): String = withContext(Dispatchers.IO) {
        val url = URL("${config.effectiveBaseUrl()}/v1/messages")
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("x-api-key", config.apiKey)
            connection.setRequestProperty("anthropic-version", "2023-06-01")
            connection.doOutput = true

            val request = AnthropicRequest(
                model = config.effectiveModel(),
                max_tokens = config.maxTokens,
                system = systemPrompt,
                messages = listOf(
                    AnthropicMessage(role = "user", content = userMessage)
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
                    provider = LlmProvider.ANTHROPIC,
                    statusCode = responseCode,
                    message = "Anthropic API error: $errorBody"
                )
            }

            val responseBody = connection.inputStream.bufferedReader().readText()
            val response = json.decodeFromString<AnthropicResponse>(responseBody)

            response.content.firstOrNull()?.text
                ?: throw LlmApiException(
                    provider = LlmProvider.ANTHROPIC,
                    statusCode = responseCode,
                    message = "Empty response from Anthropic"
                )
        } finally {
            connection.disconnect()
        }
    }
}

@Serializable
private data class AnthropicRequest(
    val model: String,
    val max_tokens: Int,
    val system: String? = null,
    val messages: List<AnthropicMessage>
)

@Serializable
private data class AnthropicMessage(
    val role: String,
    val content: String
)

@Serializable
private data class AnthropicResponse(
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
