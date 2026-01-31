package com.example.genui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.JsonElement

interface A2UiContentGenerator {
    suspend fun generate(
        document: A2UiDocument,
        state: A2UiState,
        event: A2UiEvent?
    ): A2UiDocument
}

class A2UiSession(
    initialDocument: A2UiDocument,
    private val generator: A2UiContentGenerator
) {
    private val _document = MutableStateFlow(initialDocument)
    val document: StateFlow<A2UiDocument> = _document

    private val _state = MutableStateFlow(A2UiState())
    val state: StateFlow<A2UiState> = _state

    suspend fun handleEvent(event: A2UiEvent) {
        _state.update { reduceState(it, event) }
        val next = generator.generate(_document.value, _state.value, event)
        _document.value = next
    }

    private fun reduceState(current: A2UiState, event: A2UiEvent): A2UiState {
        if (event.action != "input") return current
        val value = event.payload["value"] ?: return current
        return current.withValue(event.nodeId, value)
    }
}

fun A2UiState.valueOrNull(key: String): JsonElement? = value(key)
