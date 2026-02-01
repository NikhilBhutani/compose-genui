package com.example.genui.llm

/**
 * Configuration for LLM-based A2UI content generation.
 *
 * @param provider The LLM provider to use
 * @param apiKey API key for the provider
 * @param model Model name (provider-specific, uses defaults if null)
 * @param systemPrompt Custom system prompt (uses default A2UI prompt if null)
 * @param maxTokens Maximum tokens for response
 * @param temperature Sampling temperature (0.0-1.0)
 * @param baseUrl Custom API base URL (for proxies or self-hosted)
 */
data class A2UiLlmConfig(
    val provider: LlmProvider,
    val apiKey: String,
    val model: String? = null,
    val systemPrompt: String? = null,
    val maxTokens: Int = 4096,
    val temperature: Float = 0.7f,
    val baseUrl: String? = null
) {
    /**
     * Get the effective model name, using provider defaults if not specified.
     */
    fun effectiveModel(): String = model ?: provider.defaultModel

    /**
     * Get the effective base URL for the provider.
     */
    fun effectiveBaseUrl(): String = baseUrl ?: provider.defaultBaseUrl
}

/**
 * Supported LLM providers.
 */
enum class LlmProvider(
    val defaultModel: String,
    val defaultBaseUrl: String
) {
    ANTHROPIC(
        defaultModel = "claude-sonnet-4-20250514",
        defaultBaseUrl = "https://api.anthropic.com"
    ),
    OPENAI(
        defaultModel = "gpt-4o",
        defaultBaseUrl = "https://api.openai.com"
    ),
    GEMINI(
        defaultModel = "gemini-2.0-flash",
        defaultBaseUrl = "https://generativelanguage.googleapis.com"
    )
}

/**
 * Default system prompt for A2UI generation.
 */
object A2UiPrompts {
    val DEFAULT_SYSTEM_PROMPT = """
You are an A2UI (Agent-to-User Interface) generator. You generate JSON documents that describe user interfaces.

The A2UI format is a JSON structure with a root node containing nested components. Each component has:
- type: Component type (e.g., "column", "text", "button")
- id: Optional unique identifier for interactive components
- props: Component properties (varies by type)
- children: Nested child components (for containers)

Available components:
- Layout: column, row, box, surface, card, elevatedCard, outlinedCard, spacer, divider, list, listRow, listItem, listItemM3, scaffold, scrollColumn, scrollRow
- Inputs: textfield, button, elevatedButton, tonalButton, iconButton, checkbox, triStateCheckbox, radio, switch, slider, rangeSlider, stepper, chip, filterChip, inputChip, suggestionChip, progress, segmentedButton, segment, searchBar, dropdown, option, datePicker, timePicker
- Navigation: topAppBar, centerTopAppBar, mediumTopAppBar, largeTopAppBar, bottomAppBar, navigationBar, navItem, navigationRail, railItem, navigationDrawer, drawerItem, tabs, tab, menu, menuItem, dialog, bottomSheet
- Paging: horizontalPager, verticalPager, page
- Text: text
- Media: image, icon, avatar
- Feedback: snackbar, badge, fab, banner, tooltip, richTooltip
- Gestures: swipeToDismiss

Common props:
- padding, spacing, fill, width, height for layout
- text, fontSize, fontWeight, color for text
- label, enabled, variant for buttons
- icon for icon-related components

When receiving events, update the UI accordingly. Always respond with valid A2UI JSON.

Example response:
```json
{
  "root": {
    "type": "column",
    "props": { "padding": 16, "spacing": 8 },
    "children": [
      { "type": "text", "props": { "text": "Hello!", "fontSize": 24, "fontWeight": "bold" } },
      { "type": "button", "id": "greet", "props": { "label": "Click me" } }
    ]
  }
}
```

Respond ONLY with the JSON document, no explanation or markdown.
""".trimIndent()

    /**
     * Build a user message for the LLM including current state and event.
     */
    fun buildUserMessage(
        currentDocument: String,
        state: Map<String, Any?>,
        event: String?,
        userIntent: String?
    ): String = buildString {
        append("Current UI state:\n")
        append(currentDocument)
        append("\n\n")

        if (state.isNotEmpty()) {
            append("Form state: ")
            append(state.entries.joinToString(", ") { "${it.key}=${it.value}" })
            append("\n\n")
        }

        if (event != null) {
            append("User event: $event\n\n")
        }

        if (userIntent != null) {
            append("User request: $userIntent\n\n")
        }

        append("Generate the updated A2UI document.")
    }
}
