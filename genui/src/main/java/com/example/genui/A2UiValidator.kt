package com.example.genui

import kotlinx.serialization.json.JsonObject

sealed class A2UiValidationIssue(open val path: String, open val message: String) {
    data class Error(override val path: String, override val message: String) : A2UiValidationIssue(path, message)
    data class Warning(override val path: String, override val message: String) : A2UiValidationIssue(path, message)
}

data class A2UiValidationResult(
    val issues: List<A2UiValidationIssue>
) {
    val isValid: Boolean = issues.none { it is A2UiValidationIssue.Error }
}

fun validateA2Ui(
    document: A2UiDocument,
    schema: A2UiSchema = defaultA2UiSchema(),
    strict: Boolean = false
): A2UiValidationResult {
    val issues = mutableListOf<A2UiValidationIssue>()

    fun validateNode(node: A2UiNode, path: String) {
        val spec = schema.components[node.type]
        if (spec == null) {
            issues += A2UiValidationIssue.Error(path, "Unknown component type: ${node.type}")
        } else {
            validateProps(node.props, spec, path)
            if (!spec.allowChildren && node.children.isNotEmpty()) {
                issues += A2UiValidationIssue.Warning(path, "${node.type} should not have children")
            }
        }

        node.children.forEachIndexed { index, child ->
            validateNode(child, "$path.children[$index]")
        }
    }

    fun validateProps(props: JsonObject, spec: A2UiComponentSpec, path: String) {
        val keys = props.keys
        val missing = spec.requiredProps.filter { it !in keys }
        if (missing.isNotEmpty()) {
            issues += A2UiValidationIssue.Error(
                path,
                "Missing required props for ${spec.type}: ${missing.joinToString(", ")}" 
            )
        }

        if (strict) {
            val allowed = spec.requiredProps + spec.optionalProps
            val unknown = keys.filterNot { it in allowed }
            if (unknown.isNotEmpty()) {
                issues += A2UiValidationIssue.Warning(
                    path,
                    "Unknown props for ${spec.type}: ${unknown.joinToString(", ")}" 
                )
            }
        }
    }

    validateNode(document.root, "root")
    return A2UiValidationResult(issues)
}
