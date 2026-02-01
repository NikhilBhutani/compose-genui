package com.example.genui.llm

import org.junit.Assert.*
import org.junit.Test

class A2UiLlmConfigTest {

    @Test
    fun `default model is used when not specified`() {
        val config = A2UiLlmConfig(
            provider = LlmProvider.ANTHROPIC,
            apiKey = "test-key"
        )
        
        assertEquals("claude-sonnet-4-20250514", config.effectiveModel())
    }

    @Test
    fun `custom model overrides default`() {
        val config = A2UiLlmConfig(
            provider = LlmProvider.OPENAI,
            apiKey = "test-key",
            model = "gpt-4-turbo"
        )
        
        assertEquals("gpt-4-turbo", config.effectiveModel())
    }

    @Test
    fun `default base URL is used when not specified`() {
        val anthropicConfig = A2UiLlmConfig(
            provider = LlmProvider.ANTHROPIC,
            apiKey = "test-key"
        )
        assertEquals("https://api.anthropic.com", anthropicConfig.effectiveBaseUrl())
        
        val openAiConfig = A2UiLlmConfig(
            provider = LlmProvider.OPENAI,
            apiKey = "test-key"
        )
        assertEquals("https://api.openai.com", openAiConfig.effectiveBaseUrl())
        
        val geminiConfig = A2UiLlmConfig(
            provider = LlmProvider.GEMINI,
            apiKey = "test-key"
        )
        assertEquals("https://generativelanguage.googleapis.com", geminiConfig.effectiveBaseUrl())
    }

    @Test
    fun `custom base URL overrides default`() {
        val config = A2UiLlmConfig(
            provider = LlmProvider.OPENAI,
            apiKey = "test-key",
            baseUrl = "https://my-proxy.example.com"
        )
        
        assertEquals("https://my-proxy.example.com", config.effectiveBaseUrl())
    }

    @Test
    fun `factory creates correct generator type`() {
        val anthropicGen = A2UiLlmGeneratorFactory.create(
            provider = LlmProvider.ANTHROPIC,
            apiKey = "test"
        )
        assertTrue(anthropicGen is AnthropicGenerator)
        
        val openAiGen = A2UiLlmGeneratorFactory.create(
            provider = LlmProvider.OPENAI,
            apiKey = "test"
        )
        assertTrue(openAiGen is OpenAiGenerator)
        
        val geminiGen = A2UiLlmGeneratorFactory.create(
            provider = LlmProvider.GEMINI,
            apiKey = "test"
        )
        assertTrue(geminiGen is GeminiGenerator)
    }

    @Test
    fun `DSL builder creates valid config`() {
        val generator = a2uiLlmGenerator {
            provider = LlmProvider.ANTHROPIC
            apiKey = "my-api-key"
            model = "claude-3-opus-20240229"
            temperature = 0.5f
            maxTokens = 2048
        }
        
        assertNotNull(generator)
        assertTrue(generator is AnthropicGenerator)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `DSL builder fails without API key`() {
        a2uiLlmGenerator {
            provider = LlmProvider.OPENAI
            // Missing apiKey
        }
    }

    @Test
    fun `providers have expected default models`() {
        assertEquals("claude-sonnet-4-20250514", LlmProvider.ANTHROPIC.defaultModel)
        assertEquals("gpt-4o", LlmProvider.OPENAI.defaultModel)
        assertEquals("gemini-2.0-flash", LlmProvider.GEMINI.defaultModel)
    }

    @Test
    fun `system prompt default is not empty`() {
        val prompt = A2UiPrompts.DEFAULT_SYSTEM_PROMPT
        assertTrue(prompt.isNotBlank())
        assertTrue(prompt.contains("A2UI"))
        assertTrue(prompt.contains("JSON"))
    }

    @Test
    fun `user message builder includes document`() {
        val message = A2UiPrompts.buildUserMessage(
            currentDocument = """{"root":{"type":"text"}}""",
            state = emptyMap(),
            event = null,
            userIntent = null
        )
        
        assertTrue(message.contains("root"))
        assertTrue(message.contains("text"))
    }

    @Test
    fun `user message builder includes state`() {
        val message = A2UiPrompts.buildUserMessage(
            currentDocument = "{}",
            state = mapOf("name" to "John", "age" to 30),
            event = null,
            userIntent = null
        )
        
        assertTrue(message.contains("name=John"))
        assertTrue(message.contains("age=30"))
    }

    @Test
    fun `user message builder includes event`() {
        val message = A2UiPrompts.buildUserMessage(
            currentDocument = "{}",
            state = emptyMap(),
            event = "submitButton:click",
            userIntent = null
        )
        
        assertTrue(message.contains("submitButton:click"))
    }

    @Test
    fun `user message builder includes user intent`() {
        val message = A2UiPrompts.buildUserMessage(
            currentDocument = "{}",
            state = emptyMap(),
            event = null,
            userIntent = "Add a login form"
        )
        
        assertTrue(message.contains("Add a login form"))
    }
}
