package com.example.genui.llm

/**
 * Factory for creating LLM-based A2UI generators.
 *
 * Example usage:
 * ```kotlin
 * // Using Anthropic
 * val generator = A2UiLlmGeneratorFactory.create(
 *     provider = LlmProvider.ANTHROPIC,
 *     apiKey = "your-api-key"
 * )
 *
 * // Using OpenAI with custom model
 * val generator = A2UiLlmGeneratorFactory.create(
 *     provider = LlmProvider.OPENAI,
 *     apiKey = "your-api-key",
 *     model = "gpt-4-turbo"
 * )
 *
 * // Using Gemini
 * val generator = A2UiLlmGeneratorFactory.create(
 *     provider = LlmProvider.GEMINI,
 *     apiKey = "your-api-key"
 * )
 * ```
 */
object A2UiLlmGeneratorFactory {

    /**
     * Create a generator for the specified provider.
     *
     * @param provider LLM provider to use
     * @param apiKey API key for authentication
     * @param model Model name (optional, uses provider default)
     * @param systemPrompt Custom system prompt (optional)
     * @param maxTokens Maximum response tokens
     * @param temperature Sampling temperature
     * @param baseUrl Custom API base URL (for proxies)
     */
    fun create(
        provider: LlmProvider,
        apiKey: String,
        model: String? = null,
        systemPrompt: String? = null,
        maxTokens: Int = 4096,
        temperature: Float = 0.7f,
        baseUrl: String? = null
    ): A2UiLlmGenerator {
        val config = A2UiLlmConfig(
            provider = provider,
            apiKey = apiKey,
            model = model,
            systemPrompt = systemPrompt,
            maxTokens = maxTokens,
            temperature = temperature,
            baseUrl = baseUrl
        )
        return create(config)
    }

    /**
     * Create a generator from a configuration object.
     */
    fun create(config: A2UiLlmConfig): A2UiLlmGenerator {
        return when (config.provider) {
            LlmProvider.ANTHROPIC -> AnthropicGenerator(config)
            LlmProvider.OPENAI -> OpenAiGenerator(config)
            LlmProvider.GEMINI -> GeminiGenerator(config)
        }
    }

    /**
     * Create generators for multiple providers (for fallback/load balancing).
     */
    fun createMultiple(configs: List<A2UiLlmConfig>): List<A2UiLlmGenerator> {
        return configs.map { create(it) }
    }
}

/**
 * DSL builder for creating LLM configurations.
 *
 * Example:
 * ```kotlin
 * val generator = a2uiLlmGenerator {
 *     provider = LlmProvider.ANTHROPIC
 *     apiKey = "your-key"
 *     model = "claude-3-opus-20240229"
 *     temperature = 0.5f
 * }
 * ```
 */
fun a2uiLlmGenerator(block: A2UiLlmConfigBuilder.() -> Unit): A2UiLlmGenerator {
    val builder = A2UiLlmConfigBuilder()
    builder.block()
    return A2UiLlmGeneratorFactory.create(builder.build())
}

class A2UiLlmConfigBuilder {
    var provider: LlmProvider = LlmProvider.OPENAI
    var apiKey: String = ""
    var model: String? = null
    var systemPrompt: String? = null
    var maxTokens: Int = 4096
    var temperature: Float = 0.7f
    var baseUrl: String? = null

    fun build(): A2UiLlmConfig {
        require(apiKey.isNotBlank()) { "API key is required" }
        return A2UiLlmConfig(
            provider = provider,
            apiKey = apiKey,
            model = model,
            systemPrompt = systemPrompt,
            maxTokens = maxTokens,
            temperature = temperature,
            baseUrl = baseUrl
        )
    }
}
