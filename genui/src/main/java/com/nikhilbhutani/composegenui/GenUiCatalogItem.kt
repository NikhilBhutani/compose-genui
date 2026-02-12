package com.nikhilbhutani.composegenui

/**
 * Bundles a component type with its schema specification (sent to the LLM)
 * and its renderer (used to render A2UI nodes).
 */
data class GenUiCatalogItem(
    val type: String,
    val spec: A2UiComponentSpec,
    val renderer: A2UiRenderer
)
