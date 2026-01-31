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
        "contentAlignment"
    )
    val textStyleProps = setOf(
        "color",
        "fontSize",
        "fontWeight",
        "textAlign",
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
            optionalProps = commonLayoutProps,
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "card",
            optionalProps = commonLayoutProps,
            allowChildren = true
        ),
        A2UiComponentSpec(
            type = "list",
            optionalProps = commonLayoutProps,
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
            optionalProps = commonLayoutProps + setOf("label", "enabled"),
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
