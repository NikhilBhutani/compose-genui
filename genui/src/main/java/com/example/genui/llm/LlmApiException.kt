package com.example.genui.llm

/**
 * Exception thrown when an LLM API call fails.
 *
 * @param provider The LLM provider that failed
 * @param statusCode HTTP status code (if available)
 * @param message Error message
 * @param cause Underlying cause (if any)
 */
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

/**
 * Exception thrown when parsing LLM response fails.
 */
class A2UiParseException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause)
