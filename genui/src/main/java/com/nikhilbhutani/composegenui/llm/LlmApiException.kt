package com.nikhilbhutani.composegenui.llm

class LlmApiException(
    val provider: LlmProvider,
    val statusCode: Int? = null,
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    override fun toString(): String = buildString {
        append("LlmApiException(provider=$provider")
        if (statusCode != null) append(", statusCode=$statusCode")
        append(", message=$message)")
    }
}

class A2UiParseException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause)
