package com.nikhilbhutani.composegenui.llm

/** Exception thrown when an LLM API call fails. */
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

/** Exception thrown when an LLM response cannot be parsed as valid A2UI JSON. */
class A2UiParseException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause)
