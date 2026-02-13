package com.nikhilbhutani.composegenui

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** JSON serialization utilities for A2UI documents. */
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

/** Convenience function to parse a JSON string into an [A2UiDocument]. */
fun parseA2UiDocument(json: String): A2UiDocument =
    A2UiJson.decodeDocument(json)

fun A2UiDocument.toJson(): String =
    A2UiJson.encodeDocument(this)
