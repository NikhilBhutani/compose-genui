package com.example.genui

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object A2UiJson {
    val instance: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }

    fun decodeDocument(raw: String): A2UiDocument =
        instance.decodeFromString(A2UiDocument.serializer(), raw)

    fun encodeDocument(document: A2UiDocument): String =
        instance.encodeToString(document)
}

/**
 * Parse a JSON string into an A2UiDocument.
 * Top-level function for convenience.
 */
fun parseA2UiDocument(json: String): A2UiDocument =
    A2UiJson.decodeDocument(json)

/**
 * Serialize an A2UiDocument to JSON string.
 */
fun A2UiDocument.toJson(): String =
    A2UiJson.encodeDocument(this)
