package com.example.genui

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

internal fun JsonObject.sp(key: String): TextUnit? =
    float(key)?.sp ?: int(key)?.sp

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

internal fun JsonObject.cornerRadius(): Dp? = dp("cornerRadius")

internal fun JsonObject.borderWidth(): Dp? = dp("borderWidth")

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

internal fun JsonObject.contentAlignment(): Alignment? = when (string("contentAlignment")) {
    "topStart" -> Alignment.TopStart
    "topCenter" -> Alignment.TopCenter
    "topEnd" -> Alignment.TopEnd
    "centerStart" -> Alignment.CenterStart
    "center" -> Alignment.Center
    "centerEnd" -> Alignment.CenterEnd
    "bottomStart" -> Alignment.BottomStart
    "bottomCenter" -> Alignment.BottomCenter
    "bottomEnd" -> Alignment.BottomEnd
    else -> null
}

internal fun JsonObject.textAlign(): TextAlign? = when (string("textAlign")) {
    "start" -> TextAlign.Start
    "center" -> TextAlign.Center
    "end" -> TextAlign.End
    "justify" -> TextAlign.Justify
    else -> null
}

internal fun JsonObject.contentScale(): ContentScale? = when (string("contentScale")) {
    "fit" -> ContentScale.Fit
    "crop" -> ContentScale.Crop
    "fillBounds" -> ContentScale.FillBounds
    "fillWidth" -> ContentScale.FillWidth
    "fillHeight" -> ContentScale.FillHeight
    "inside" -> ContentScale.Inside
    else -> null
}

internal fun JsonObject.fontWeight(): FontWeight? {
    val raw = string("fontWeight") ?: return null
    return when (raw) {
        "thin" -> FontWeight.Thin
        "extraLight" -> FontWeight.ExtraLight
        "light" -> FontWeight.Light
        "normal" -> FontWeight.Normal
        "medium" -> FontWeight.Medium
        "semiBold" -> FontWeight.SemiBold
        "bold" -> FontWeight.Bold
        "extraBold" -> FontWeight.ExtraBold
        "black" -> FontWeight.Black
        else -> raw.toIntOrNull()?.let { FontWeight(it) }
    }
}

internal fun JsonObject.fontStyle(): FontStyle? = when (string("fontStyle")) {
    "italic" -> FontStyle.Italic
    "normal" -> FontStyle.Normal
    else -> null
}

internal fun JsonObject.lineHeight(): TextUnit? = sp("lineHeight")

internal fun JsonObject.letterSpacing(): TextUnit? = sp("letterSpacing")

internal fun JsonObject.color(key: String): Color? =
    string(key)?.let { parseColor(it) }

internal fun JsonElement.asStringOrNull(): String? =
    (this as? JsonPrimitive)?.contentOrNull

internal data class A2UiPadding(
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp
)

internal fun parseColor(value: String): Color? {
    val trimmed = value.trim()
    if (!trimmed.startsWith("#")) return null
    val hex = trimmed.removePrefix("#")
    return when (hex.length) {
        6 -> hex.toLongOrNull(16)?.let { Color((0xFF000000 or it).toInt()) }
        8 -> hex.toLongOrNull(16)?.let { Color(it.toInt()) }
        else -> null
    }
}
