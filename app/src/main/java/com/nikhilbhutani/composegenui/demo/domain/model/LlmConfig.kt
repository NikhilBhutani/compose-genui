package com.nikhilbhutani.composegenui.demo.domain.model

import com.nikhilbhutani.composegenui.demo.data.LlmProviderOption

data class LlmConfig(
    val provider: LlmProviderOption,
    val apiKey: String,
    val model: String
)
