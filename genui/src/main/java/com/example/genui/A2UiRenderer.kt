package com.example.genui

import androidx.compose.runtime.Composable

@Composable
fun A2UiRender(
    document: A2UiDocument,
    catalog: A2UiCatalog,
    state: A2UiState,
    onEvent: (A2UiEvent) -> Unit
) {
    RenderNode(document.root, catalog, state, onEvent)
}

@Composable
fun RenderNode(
    node: A2UiNode,
    catalog: A2UiCatalog,
    state: A2UiState,
    onEvent: (A2UiEvent) -> Unit
) {
    catalog.Render(node, state, onEvent) { child ->
        RenderNode(child, catalog, state, onEvent)
    }
}
