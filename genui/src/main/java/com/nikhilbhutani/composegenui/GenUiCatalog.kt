package com.nikhilbhutani.composegenui

/**
 * Unified catalog that combines schema specs and renderers for all registered components.
 * This is the single source of truth that feeds both the LLM system prompt and the renderer.
 */
class GenUiCatalog(
    private val items: List<GenUiCatalogItem>
) {
    private val itemsByType: Map<String, GenUiCatalogItem> =
        items.associateBy { it.type }

    /** Extracts the renderer map for use with [A2UiCatalogRegistry]. */
    fun asA2UiCatalog(): A2UiCatalog =
        A2UiCatalogRegistry(items.associate { it.type to it.renderer })

    /** Extracts the schema map for validation. */
    fun asA2UiSchema(): A2UiSchema =
        A2UiSchema(items.associate { it.type to it.spec })

    /**
     * Generates a structured text description of all components and their props,
     * grouped by category. This is injected into the LLM system prompt so the model
     * knows exactly which components are available and how to use them.
     */
    fun toSchemaDescription(): String = buildString {
        val categories = items.groupBy { categorize(it.type) }

        for ((category, categoryItems) in categories) {
            appendLine("## $category")
            for (item in categoryItems) {
                val spec = item.spec
                append("- **${spec.type}**")
                if (!spec.allowChildren) append(" (leaf)")
                appendLine()

                if (spec.requiredProps.isNotEmpty()) {
                    appendLine("  Required: ${spec.requiredProps.sorted().joinToString(", ")}")
                }
                if (spec.optionalProps.isNotEmpty()) {
                    appendLine("  Optional: ${spec.optionalProps.sorted().joinToString(", ")}")
                }
            }
            appendLine()
        }
    }

    /** Merge two catalogs. Items in [other] override items with the same type. */
    operator fun plus(other: GenUiCatalog): GenUiCatalog {
        val merged = itemsByType.toMutableMap()
        other.items.forEach { merged[it.type] = it }
        return GenUiCatalog(merged.values.toList())
    }

    private fun categorize(type: String): String = when (type) {
        "column", "row", "box", "surface", "card", "elevatedCard", "outlinedCard",
        "spacer", "divider", "scaffold", "scrollColumn", "scrollRow",
        "list", "listRow", "listItem" -> "Layout"

        "textfield", "button", "elevatedButton", "tonalButton", "textButton",
        "outlinedButton", "iconButton", "filledIconButton", "outlinedIconButton",
        "iconToggleButton", "checkbox", "triStateCheckbox", "radio", "switch",
        "slider", "rangeSlider", "stepper", "segmentedButton", "segment" -> "Input"

        "chip", "filterChip", "inputChip", "suggestionChip",
        "searchBar", "dropdown", "option" -> "Chips & Selection"

        "topAppBar", "centerTopAppBar", "mediumTopAppBar", "largeTopAppBar",
        "bottomAppBar", "navigationBar", "navItem", "navigationRail", "railItem",
        "navigationDrawer", "drawerItem", "tabs", "tab", "menu", "menuItem",
        "dialog", "bottomSheet", "bottomSheetScaffold" -> "Navigation"

        "text", "image", "icon", "avatar", "listItemM3" -> "Media & Display"

        "progress", "snackbar", "badge", "fab", "banner", "tooltip",
        "richTooltip", "swipeToDismiss", "pullToRefresh" -> "Feedback"

        "horizontalPager", "verticalPager", "page",
        "datePicker", "timePicker" -> "Paging & Pickers"

        else -> "Other"
    }
}
