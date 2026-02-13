package com.nikhilbhutani.composegenui

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull

/** Severity level for validation issues. */
enum class ValidationSeverity { ERROR, WARNING, INFO }

/** A single validation issue found in an A2UI document. */
sealed class A2UiValidationIssue(
    open val path: String,
    open val message: String,
    open val severity: ValidationSeverity
) {
    data class Error(override val path: String, override val message: String) :
        A2UiValidationIssue(path, message, ValidationSeverity.ERROR)
    data class Warning(override val path: String, override val message: String) :
        A2UiValidationIssue(path, message, ValidationSeverity.WARNING)
    data class Info(override val path: String, override val message: String) :
        A2UiValidationIssue(path, message, ValidationSeverity.INFO)
}

/** Aggregated result of validating an A2UI document against a schema. */
data class A2UiValidationResult(
    val issues: List<A2UiValidationIssue>
) {
    val isValid: Boolean = issues.none { it is A2UiValidationIssue.Error }
    val hasWarnings: Boolean = issues.any { it is A2UiValidationIssue.Warning }
    val errors: List<A2UiValidationIssue.Error>
        get() = issues.filterIsInstance<A2UiValidationIssue.Error>()
    val warnings: List<A2UiValidationIssue.Warning>
        get() = issues.filterIsInstance<A2UiValidationIssue.Warning>()

    override fun toString(): String = buildString {
        if (isValid) {
            append("Valid")
            if (hasWarnings) append(" with ${warnings.size} warning(s)")
        } else {
            append("Invalid: ${errors.size} error(s)")
        }
    }
}

/** Options controlling which validation checks are performed. */
data class A2UiValidationOptions(
    val strict: Boolean = false,
    val checkIds: Boolean = true,
    val checkStructure: Boolean = true,
    val checkValues: Boolean = true,
    val checkIcons: Boolean = true
)

fun validateA2Ui(
    document: A2UiDocument,
    schema: A2UiSchema = defaultA2UiSchema(),
    strict: Boolean = false
): A2UiValidationResult {
    return validateA2Ui(
        document = document,
        schema = schema,
        options = A2UiValidationOptions(strict = strict)
    )
}

fun validateA2Ui(
    document: A2UiDocument,
    schema: A2UiSchema = defaultA2UiSchema(),
    options: A2UiValidationOptions
): A2UiValidationResult {
    val validator = A2UiValidator(schema, options)
    return validator.validate(document)
}

/** Validates an [A2UiDocument] against an [A2UiSchema], checking types, props, structure, and values. */
class A2UiValidator(
    private val schema: A2UiSchema,
    private val options: A2UiValidationOptions
) {
    private val issues = mutableListOf<A2UiValidationIssue>()
    private val seenIds = mutableSetOf<String>()

    fun validate(document: A2UiDocument): A2UiValidationResult {
        issues.clear()
        seenIds.clear()
        validateNode(document.root, "root", null)
        return A2UiValidationResult(issues.toList())
    }

    private fun validateNode(node: A2UiNode, path: String, parent: A2UiNode?) {
        val spec = schema.components[node.type]
        if (spec == null) {
            issues += A2UiValidationIssue.Error(path, "Unknown component type: ${node.type}")
            node.children.forEachIndexed { index, child ->
                validateNode(child, "$path.children[$index]", node)
            }
            return
        }

        if (options.checkIds && node.id != null) {
            if (node.id in seenIds) {
                issues += A2UiValidationIssue.Error(path, "Duplicate ID: ${node.id}")
            } else {
                seenIds += node.id
            }
        }

        validateProps(node.props, spec, path)

        if (!spec.allowChildren && node.children.isNotEmpty()) {
            issues += A2UiValidationIssue.Warning(path, "${node.type} should not have children")
        }

        if (options.checkStructure) {
            validateStructure(node, spec, path, parent)
        }

        if (options.checkValues) {
            validatePropValues(node, path)
        }

        node.children.forEachIndexed { index, child ->
            validateNode(child, "$path.children[$index]", node)
        }
    }

    private fun validateProps(props: JsonObject, spec: A2UiComponentSpec, path: String) {
        val keys = props.keys
        val missing = spec.requiredProps.filter { it !in keys }
        if (missing.isNotEmpty()) {
            issues += A2UiValidationIssue.Error(path, "Missing required props for ${spec.type}: ${missing.joinToString(", ")}")
        }
        if (options.strict) {
            val allowed = spec.requiredProps + spec.optionalProps
            val unknown = keys.filterNot { it in allowed }
            if (unknown.isNotEmpty()) {
                issues += A2UiValidationIssue.Warning(path, "Unknown props for ${spec.type}: ${unknown.joinToString(", ")}")
            }
        }
    }

    private fun validateStructure(node: A2UiNode, spec: A2UiComponentSpec, path: String, parent: A2UiNode?) {
        when (node.type) {
            "segment" -> if (parent?.type != "segmentedButton") {
                issues += A2UiValidationIssue.Warning(path, "segment should be a direct child of segmentedButton")
            }
            "tab" -> if (parent?.type != "tabs") {
                issues += A2UiValidationIssue.Warning(path, "tab should be a direct child of tabs")
            }
            "navItem" -> if (parent?.type != "navigationBar") {
                issues += A2UiValidationIssue.Warning(path, "navItem should be a direct child of navigationBar")
            }
            "railItem" -> if (parent?.type != "navigationRail") {
                issues += A2UiValidationIssue.Warning(path, "railItem should be a direct child of navigationRail")
            }
            "drawerItem" -> if (parent?.type != "navigationDrawer") {
                issues += A2UiValidationIssue.Warning(path, "drawerItem should be a direct child of navigationDrawer")
            }
            "menuItem" -> if (parent?.type != "menu") {
                issues += A2UiValidationIssue.Warning(path, "menuItem should be a direct child of menu")
            }
            "option" -> if (parent?.type != "dropdown") {
                issues += A2UiValidationIssue.Warning(path, "option should be a direct child of dropdown")
            }
            "page" -> if (parent?.type !in listOf("horizontalPager", "verticalPager")) {
                issues += A2UiValidationIssue.Warning(path, "page should be a direct child of horizontalPager or verticalPager")
            }
        }
        if (node.id == null && isInteractiveComponent(node.type)) {
            issues += A2UiValidationIssue.Info(path, "Interactive component '${node.type}' should have an 'id' for event handling")
        }
    }

    private fun validatePropValues(node: A2UiNode, path: String) {
        val props = node.props
        if (options.checkIcons) {
            val iconProps = mutableListOf("icon", "navIcon", "leadingIcon", "trailingIcon")
            if (node.type == "icon" || node.type == "avatar") iconProps += "name"
            iconProps.forEach { propName ->
                props[propName]?.let { value ->
                    if (value is JsonPrimitive && value.isString) {
                        val iconName = value.content
                        if (iconName !in VALID_ICON_NAMES) {
                            issues += A2UiValidationIssue.Warning(path, "Unknown icon name: '$iconName'")
                        }
                    }
                }
            }
        }
        props["padding"]?.validatePositiveNumber(path, "padding")
        props["spacing"]?.validatePositiveNumber(path, "spacing")
        props["elevation"]?.validatePositiveNumber(path, "elevation")
        props["fontSize"]?.validatePositiveNumber(path, "fontSize")
        props["cornerRadius"]?.validatePositiveNumber(path, "cornerRadius")

        props["value"]?.let { value ->
            if (node.type == "progress" && value is JsonPrimitive) {
                val floatVal = value.floatOrNull
                if (floatVal != null && (floatVal < 0f || floatVal > 1f)) {
                    issues += A2UiValidationIssue.Warning(path, "progress value should be between 0 and 1")
                }
            }
        }
        props["variant"]?.let { value ->
            if (value is JsonPrimitive && value.isString) {
                val validVariants = when (node.type) {
                    "button" -> listOf("filled", "outlined", "text")
                    "textfield" -> listOf("outlined", "filled")
                    "fab" -> listOf("regular", "small", "large", "extended")
                    "progress" -> listOf("circular", "linear")
                    else -> null
                }
                if (validVariants != null && value.content !in validVariants) {
                    issues += A2UiValidationIssue.Warning(path, "Invalid variant '${value.content}' for ${node.type}. Valid: ${validVariants.joinToString(", ")}")
                }
            }
        }
    }

    private fun JsonElement.validatePositiveNumber(path: String, propName: String) {
        if (this is JsonPrimitive) {
            val num = floatOrNull ?: intOrNull?.toFloat()
            if (num != null && num < 0) {
                issues += A2UiValidationIssue.Warning(path, "$propName should be a positive number")
            }
        }
    }

    private fun isInteractiveComponent(type: String): Boolean = type in INTERACTIVE_COMPONENTS

    companion object {
        private val INTERACTIVE_COMPONENTS = setOf(
            "button", "iconButton", "filledIconButton", "outlinedIconButton", "iconToggleButton",
            "elevatedButton", "tonalButton", "textButton", "outlinedButton",
            "textfield", "checkbox", "triStateCheckbox", "radio", "switch",
            "slider", "rangeSlider", "stepper", "chip", "filterChip", "inputChip", "suggestionChip",
            "segmentedButton", "searchBar", "dropdown", "datePicker", "timePicker",
            "fab", "navItem", "railItem", "drawerItem", "tab", "menuItem",
            "topAppBar", "centerTopAppBar", "mediumTopAppBar", "largeTopAppBar",
            "dialog", "bottomSheet", "snackbar", "banner", "tooltip", "richTooltip",
            "swipeToDismiss", "pullToRefresh"
        )

        private val VALID_ICON_NAMES = setOf(
            "account", "accountCircle", "add", "arrowBack", "back", "arrowForward", "forward",
            "build", "call", "check", "clear", "close", "create", "dateRange", "calendar",
            "delete", "done", "edit", "email", "mail", "exit", "logout", "face",
            "favorite", "favoriteBorder", "home", "info",
            "keyboardArrowDown", "arrowDown", "keyboardArrowLeft", "arrowLeft",
            "keyboardArrowRight", "arrowRight", "keyboardArrowUp", "arrowUp",
            "list", "location", "locationOn", "lock", "menu", "moreVert", "more",
            "notifications", "notification", "person", "phone", "place",
            "play", "playArrow", "refresh", "search", "send", "settings", "share",
            "shoppingCart", "cart", "star", "thumbUp", "like", "warning"
        )
    }
}
