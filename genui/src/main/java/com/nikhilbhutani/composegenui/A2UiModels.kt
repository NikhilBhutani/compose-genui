package com.nikhilbhutani.composegenui

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull

/** A complete A2UI document containing a tree of UI nodes. */
@Serializable
data class A2UiDocument(
    val root: A2UiNode
)

/** A single UI node in the A2UI tree. Maps to a component in the catalog. */
@Serializable
data class A2UiNode(
    val type: String,
    val id: String? = null,
    val props: JsonObject = JsonObject(emptyMap()),
    val children: List<A2UiNode> = emptyList()
)

/** An event fired by a rendered component (e.g. click, input change). */
@Serializable
data class A2UiEvent(
    val nodeId: String,
    val action: String,
    val payload: JsonObject = JsonObject(emptyMap()),
    val surfaceId: String? = null,
    val timestamp: String? = null
)

/** Holds form/input state keyed by node ID. Immutable — use [withValue] to derive new state. */
data class A2UiState(
    val values: Map<String, kotlinx.serialization.json.JsonElement> = emptyMap()
) {
    fun value(key: String): kotlinx.serialization.json.JsonElement? = values[key]

    fun string(key: String): String? =
        values[key]?.let { (it as? kotlinx.serialization.json.JsonPrimitive)?.contentOrNull }

    fun withValue(key: String, value: kotlinx.serialization.json.JsonElement): A2UiState =
        copy(values = values + (key to value))
}

/** A user interaction captured by [GenUiSurface] and fed back to the conversation. */
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

fun A2UiState.valueOrNull(key: String): JsonElement? = value(key)
