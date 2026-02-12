package com.nikhilbhutani.composegenui.demo.data

enum class LlmProviderOption(
    val label: String,
    val defaultModel: String
) {
    ANTHROPIC("Anthropic", "claude-sonnet-4-5-20250929"),
    OPENAI("OpenAI", "gpt-4o"),
    GEMINI("Gemini", "gemini-2.0-flash"),
    FIREBASE_AI("Firebase AI", "gemini-2.0-flash"),
    DEMO("Demo (Offline)", "");

    companion object {
        fun fromName(name: String): LlmProviderOption =
            entries.firstOrNull { it.name == name } ?: DEMO
    }
}

data class DemoSettings(
    val provider: LlmProviderOption = LlmProviderOption.DEMO,
    val apiKey: String = "",
    val model: String = ""
) {
    val effectiveModel: String
        get() = model.ifBlank { provider.defaultModel }
}
