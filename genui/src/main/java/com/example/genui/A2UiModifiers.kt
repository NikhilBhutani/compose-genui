package com.example.genui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier

internal fun JsonObject.toModifier(): Modifier {
    var modifier = Modifier
    val padding = padding()
    if (padding != null) {
        modifier = modifier.padding(
            start = padding.start,
            top = padding.top,
            end = padding.end,
            bottom = padding.bottom
        )
    }

    val fill = string("fill")
    when (fill) {
        "max" -> modifier = modifier.fillMaxSize()
        "width" -> modifier = modifier.fillMaxWidth()
        "height" -> modifier = modifier.fillMaxHeight()
        else -> Unit
    }

    val width = dp("width")
    val height = dp("height")
    val size = dp("size")
    if (size != null) {
        modifier = modifier.size(size)
    } else {
        if (width != null) modifier = modifier.width(width)
        if (height != null) modifier = modifier.height(height)
    }

    val bg = string("background")
    val color = bg?.let { parseColor(it) }
    if (color != null) {
        modifier = modifier.background(color)
    }

    return modifier
}
