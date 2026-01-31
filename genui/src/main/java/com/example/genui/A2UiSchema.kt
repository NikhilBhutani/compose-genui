package com.example.genui

data class A2UiComponentSpec(
    val type: String,
    val requiredProps: Set<String> = emptySet(),
    val optionalProps: Set<String> = emptySet(),
    val allowChildren: Boolean = true
)

data class A2UiSchema(
    val components: Map<String, A2UiComponentSpec>
)

fun defaultA2UiSchema(): A2UiSchema {
    val commonLayoutProps = setOf(
        "padding",
        "spacing",
        "fill",
        "width",
        "height",
        "size",
        "background",
        "horizontalAlignment",
        "verticalAlignment",
        "contentAlignment",
        "cornerRadius",
        "borderWidth",
        "borderColor"
    )
    val textStyleProps = setOf(
        "color",
        "fontSize",
        "fontWeight",
        "fontStyle",
        "textAlign",
        "lineHeight",
        "letterSpacing",
        "maxLines"
    )

    val components = listOf(
        A2UiComponentSpec(
            type = "column",
            optionalProps = commonLayoutProps,
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "row",
            optionalProps = commonLayoutProps,
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "box",
            optionalProps = commonLayoutProps,
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "surface",
            optionalProps = commonLayoutProps + setOf("elevation"),
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "card",
            optionalProps = commonLayoutProps + setOf("elevation"),
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "list",
            optionalProps = commonLayoutProps + setOf("reverse"),
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "listRow",
            optionalProps = commonLayoutProps + setOf("reverse"),
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "listItem",
            optionalProps = commonLayoutProps + setOf("spacing", "verticalAlignment"),
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "spacer",
            optionalProps = setOf("width", "height", "size"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "divider",
            optionalProps = setOf("width", "height", "fill", "padding"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "text",
            requiredProps = setOf("text"),
            optionalProps = commonLayoutProps + textStyleProps,
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "button",
            optionalProps = commonLayoutProps + setOf("label", "enabled", "variant"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "iconButton",
            optionalProps = commonLayoutProps + setOf("name", "enabled", "color", "contentDescription"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "chip",
            optionalProps = commonLayoutProps + setOf("label", "enabled", "icon"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "progress",
            optionalProps = commonLayoutProps + setOf("variant", "value"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "topAppBar",
            optionalProps = commonLayoutProps + setOf("title", "navIcon"),
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "navigationBar",
            optionalProps = commonLayoutProps,
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "navItem",
            optionalProps = commonLayoutProps + setOf("label", "icon", "selected"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "tabs",
            optionalProps = commonLayoutProps + setOf("selectedIndex"),
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "tab",
            optionalProps = commonLayoutProps + setOf("label"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "menu",
            optionalProps = commonLayoutProps + setOf("expanded"),
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "menuItem",
            optionalProps = commonLayoutProps + setOf("label"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "dialog",
            optionalProps = commonLayoutProps + setOf("title", "text", "confirmLabel", "dismissLabel"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "textfield",
            optionalProps = commonLayoutProps + setOf("label", "enabled"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "checkbox",
            optionalProps = commonLayoutProps + setOf("checked", "enabled"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "triStateCheckbox",
            optionalProps = commonLayoutProps + setOf("state", "enabled"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "switch",
            optionalProps = commonLayoutProps + setOf("checked", "enabled"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "slider",
            optionalProps = commonLayoutProps + setOf("value", "min", "max", "steps", "enabled"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "stepper",
            optionalProps = commonLayoutProps + setOf("value", "min", "max", "step"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "image",
            requiredProps = setOf("url"),
            optionalProps = commonLayoutProps + setOf("contentScale", "contentDescription"),
            allowChildren = false
        ),
        A2UiComponentSpec(
            type = "icon",
            optionalProps = commonLayoutProps + setOf("name", "contentDescription", "color"),
            allowChildren = false
        )
    )

    return A2UiSchema(components.associateBy { it.type })
}
