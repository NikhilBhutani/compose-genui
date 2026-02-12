package com.nikhilbhutani.composegenui.llm

enum class LlmProvider(
    val defaultModel: String,
    val defaultBaseUrl: String
) {
    ANTHROPIC(
        defaultModel = "claude-sonnet-4-5-20250929",
        defaultBaseUrl = "https://api.anthropic.com"
    ),
    OPENAI(
        defaultModel = "gpt-4o",
        defaultBaseUrl = "https://api.openai.com"
    ),
    GEMINI(
        defaultModel = "gemini-2.0-flash",
        defaultBaseUrl = "https://generativelanguage.googleapis.com"
    )
}
