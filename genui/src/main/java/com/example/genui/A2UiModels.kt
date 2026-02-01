package com.example.genui

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class A2UiDocument(
    val root: A2UiNode
)

@Serializable
data class A2UiNode(
    val type: String,
    val id: String? = null,
    val props: JsonObject = JsonObject(emptyMap()),
    val children: List<A2UiNode> = emptyList()
)

@Serializable
data class A2UiEvent(
    val nodeId: String,
    val action: String,
    val payload: JsonObject = JsonObject(emptyMap()),
    val surfaceId: String? = null,
    val timestamp: String? = null
)

data class A2UiState(
    val values: Map<String, kotlinx.serialization.json.JsonElement> = emptyMap()
) {
    fun value(key: String): kotlinx.serialization.json.JsonElement? = values[key]

    fun string(key: String): String? =
        values[key]?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.contentOrNull }

    fun withValue(key: String, value: kotlinx.serialization.json.JsonElement): A2UiState =
        copy(values = values + (key to value))
}

@Serializable
data class A2UiUserAction(
    val surfaceId: String,
    val sourceComponentId: String,
    val actionName: String,
    val context: JsonObject = JsonObject(emptyMap()),
    val timestamp: String? = null
)

@Serializable
data class A2UiUserActionMessage(
    val type: String = "userAction",
    val userAction: A2UiUserAction
)
