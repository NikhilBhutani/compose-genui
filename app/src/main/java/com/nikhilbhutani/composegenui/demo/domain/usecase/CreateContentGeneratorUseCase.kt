package com.nikhilbhutani.composegenui.demo.domain.usecase

import com.nikhilbhutani.composegenui.GenUiContentGenerator
import com.nikhilbhutani.composegenui.demo.data.LlmProviderOption
import com.nikhilbhutani.composegenui.demo.domain.model.LlmConfig
import com.nikhilbhutani.composegenui.demo.presentation.chat.DemoContentGenerator
import com.nikhilbhutani.composegenui.firebase.FirebaseAiContentGenerator
import com.nikhilbhutani.composegenui.llm.AnthropicContentGenerator
import com.nikhilbhutani.composegenui.llm.GeminiContentGenerator
import com.nikhilbhutani.composegenui.llm.OpenAiContentGenerator

class CreateContentGeneratorUseCase {

    operator fun invoke(config: LlmConfig): GenUiContentGenerator = when (config.provider) {
        LlmProviderOption.ANTHROPIC -> AnthropicContentGenerator(
            apiKey = config.apiKey,
            model = config.model
        )
        LlmProviderOption.OPENAI -> OpenAiContentGenerator(
            apiKey = config.apiKey,
            model = config.model
        )
        LlmProviderOption.GEMINI -> GeminiContentGenerator(
            apiKey = config.apiKey,
            model = config.model
        )
        LlmProviderOption.FIREBASE_AI -> FirebaseAiContentGenerator(
            modelName = config.model
        )
        LlmProviderOption.DEMO -> DemoContentGenerator()
    }
}
