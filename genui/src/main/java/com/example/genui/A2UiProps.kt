package com.example.genui

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

internal fun JsonObject.string(key: String): String? =
    this[key]?.jsonPrimitive?.contentOrNull

internal fun JsonObject.bool(key: String): Boolean? =
    this[key]?.jsonPrimitive?.booleanOrNull

internal fun JsonObject.int(key: String): Int? =
    this[key]?.jsonPrimitive?.intOrNull

internal fun JsonObject.float(key: String): Float? =
    this[key]?.jsonPrimitive?.floatOrNull

internal fun JsonObject.obj(key: String): JsonObject? =
    this[key] as? JsonObject

internal fun JsonObject.array(key: String): JsonArray? =
    this[key] as? JsonArray

internal fun JsonObject.dp(key: String): Dp? =
    float(key)?.dp ?: int(key)?.dp

internal fun JsonObject.padding(): A2UiPadding? {
    val all = dp("padding")
    if (all != null) {
        return A2UiPadding(all, all, all, all)
    }
    val obj = obj("padding") ?: return null
    val horizontal = obj.dp("horizontal")
    val vertical = obj.dp("vertical")
    val start = obj.dp("start") ?: horizontal
    val top = obj.dp("top") ?: vertical
    val end = obj.dp("end") ?: horizontal
    val bottom = obj.dp("bottom") ?: vertical
    if (start == null && top == null && end == null && bottom == null) return null
    return A2UiPadding(start ?: 0.dp, top ?: 0.dp, end ?: 0.dp, bottom ?: 0.dp)
}

internal fun JsonObject.spacingDp(): Dp? = dp("spacing")

internal fun JsonObject.hAlignment(): Alignment.Horizontal? = when (string("horizontalAlignment")) {
    "start" -> Alignment.Start
    "center" -> Alignment.CenterHorizontally
    "end" -> Alignment.End
    else -> null
}

internal fun JsonObject.vAlignment(): Alignment.Vertical? = when (string("verticalAlignment")) {
    "top" -> Alignment.Top
    "center" -> Alignment.CenterVertically
    "bottom" -> Alignment.Bottom
    else -> null
}

internal fun JsonElement.asStringOrNull(): String? =
    (this as? JsonPrimitive)?.contentOrNull

internal data class A2UiPadding(
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp
)
