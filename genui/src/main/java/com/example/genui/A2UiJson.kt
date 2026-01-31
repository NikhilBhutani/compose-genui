package com.example.genui

import kotlinx.serialization.json.Json

object A2UiJson {
    val instance: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    fun decodeDocument(raw: String): A2UiDocument =
        instance.decodeFromString(A2UiDocument.serializer(), raw)
}
