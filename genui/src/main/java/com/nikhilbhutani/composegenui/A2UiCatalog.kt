package com.nikhilbhutani.composegenui

import androidx.compose.runtime.Composable

/** Low-level rendering interface. Maps [A2UiNode] types to Composable renderers. */
interface A2UiCatalog {
    @Composable
    fun Render(
        node: A2UiNode,
        state: A2UiState,
        onEvent: (A2UiEvent) -> Unit,
        renderChild: @Composable (A2UiNode) -> Unit
    )
}

/** Default [A2UiCatalog] implementation backed by a map of renderers with error boundaries. */
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

/** Type alias for a Composable that renders a single [A2UiNode]. */
typealias A2UiRenderer = @Composable (
    node: A2UiNode,
    state: A2UiState,
    onEvent: (A2UiEvent) -> Unit,
    renderChild: @Composable (A2UiNode) -> Unit
) -> Unit
