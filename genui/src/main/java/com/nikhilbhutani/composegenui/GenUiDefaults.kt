package com.nikhilbhutani.composegenui

import com.nikhilbhutani.composegenui.builtins.chipBuiltins
import com.nikhilbhutani.composegenui.builtins.feedbackBuiltins
import com.nikhilbhutani.composegenui.builtins.inputBuiltins
import com.nikhilbhutani.composegenui.builtins.layoutBuiltins
import com.nikhilbhutani.composegenui.builtins.mediaBuiltins
import com.nikhilbhutani.composegenui.builtins.navigationBuiltins
import com.nikhilbhutani.composegenui.builtins.pagerBuiltins

/**
 * Creates a [GenUiCatalog] containing all built-in components with their
 * schema specifications and renderers. This is the recommended starting point
 * for most apps.
 */
fun defaultGenUiCatalog(): GenUiCatalog {
    val schema = defaultA2UiSchema()
    val renderers = layoutBuiltins() + inputBuiltins() + chipBuiltins() +
        navigationBuiltins() + feedbackBuiltins() + mediaBuiltins() + pagerBuiltins()

    val items = schema.components.map { (type, spec) ->
        val renderer = renderers[type] ?: noOpRenderer
        GenUiCatalogItem(type = type, spec = spec, renderer = renderer)
    }

    return GenUiCatalog(items)
}

private val noOpRenderer: A2UiRenderer = @androidx.compose.runtime.Composable { node, _, _, renderChild ->
    node.children.forEach { renderChild(it) }
}
