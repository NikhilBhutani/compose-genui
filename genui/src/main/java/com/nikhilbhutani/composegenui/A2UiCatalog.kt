package com.nikhilbhutani.composegenui

import androidx.compose.runtime.Composable

interface A2UiCatalog {
    @Composable
    fun Render(
        node: A2UiNode,
        state: A2UiState,
        onEvent: (A2UiEvent) -> Unit,
        renderChild: @Composable (A2UiNode) -> Unit
    )
}

class A2UiCatalogRegistry(
    private val renderers: Map<String, A2UiRenderer>
) : A2UiCatalog {
    @Composable
    override fun Render(
        node: A2UiNode,
        state: A2UiState,
        onEvent: (A2UiEvent) -> Unit,
        renderChild: @Composable (A2UiNode) -> Unit
    ) {
        val renderer = renderers[node.type]
        if (renderer != null) {
            A2UiErrorBoundary(
                fallback = { error -> DefaultErrorFallback(error, node.type) }
            ) {
                renderer(node, state, onEvent, renderChild)
            }
        } else {
            renderChildFallback(node, state, onEvent, renderChild)
        }
    }

    @Composable
    private fun renderChildFallback(
        node: A2UiNode,
        state: A2UiState,
        onEvent: (A2UiEvent) -> Unit,
        renderChild: @Composable (A2UiNode) -> Unit
    ) {
        node.children.forEach { child ->
            renderChild(child)
        }
    }
}

typealias A2UiRenderer = @Composable (
    node: A2UiNode,
    state: A2UiState,
    onEvent: (A2UiEvent) -> Unit,
    renderChild: @Composable (A2UiNode) -> Unit
) -> Unit
