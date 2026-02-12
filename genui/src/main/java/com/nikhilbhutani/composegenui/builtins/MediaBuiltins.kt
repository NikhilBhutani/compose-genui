package com.nikhilbhutani.composegenui.builtins

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nikhilbhutani.composegenui.A2UiEvent
import com.nikhilbhutani.composegenui.A2UiRenderer
import com.nikhilbhutani.composegenui.bool
import com.nikhilbhutani.composegenui.color
import com.nikhilbhutani.composegenui.contentScale
import com.nikhilbhutani.composegenui.dp
import com.nikhilbhutani.composegenui.fontStyle
import com.nikhilbhutani.composegenui.fontWeight
import com.nikhilbhutani.composegenui.int
import com.nikhilbhutani.composegenui.letterSpacing
import com.nikhilbhutani.composegenui.lineHeight
import com.nikhilbhutani.composegenui.sp
import com.nikhilbhutani.composegenui.spacingDp
import com.nikhilbhutani.composegenui.string
import com.nikhilbhutani.composegenui.textAlign
import com.nikhilbhutani.composegenui.toModifier
import com.nikhilbhutani.composegenui.vAlignment

internal fun mediaBuiltins(): Map<String, A2UiRenderer> = mapOf(
    "text" to { node, _, _, _ ->
        val text = node.props.string("text") ?: ""
        val color = node.props.color("color") ?: Color.Unspecified
        val fontSize = node.props.sp("fontSize") ?: TextUnit.Unspecified
        val fontWeight = node.props.fontWeight()
        val fontStyle = node.props.fontStyle()
        val textAlign = node.props.textAlign()
        val lineHeight = node.props.lineHeight() ?: TextUnit.Unspecified
        val letterSpacing = node.props.letterSpacing() ?: TextUnit.Unspecified
        val maxLines = node.props.int("maxLines") ?: Int.MAX_VALUE
        Text(
            text = text,
            modifier = node.props.toModifier(),
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            textAlign = textAlign,
            lineHeight = lineHeight,
            letterSpacing = letterSpacing,
            maxLines = maxLines
        )
    },
    "image" to { node, _, _, _ ->
        val url = node.props.string("url")
        if (url != null) {
            val scale = node.props.contentScale() ?: ContentScale.Fit
            AsyncImage(
                modifier = node.props.toModifier(),
                model = url,
                contentDescription = node.props.string("contentDescription"),
                contentScale = scale
            )
        }
    },
    "icon" to { node, _, _, _ ->
        val name = node.props.string("name") ?: "info"
        val vector = iconByName(name)
        if (vector != null) {
            val tint = node.props.color("color") ?: Color.Unspecified
            Icon(
                modifier = node.props.toModifier(),
                imageVector = vector,
                contentDescription = node.props.string("contentDescription"),
                tint = tint
            )
        }
    },
    "avatar" to { node, _, _, _ ->
        val size = node.props.dp("size") ?: 40.dp
        val url = node.props.string("url")
        val iconName = node.props.string("icon")
        val baseModifier = node.props.toModifier().size(size).clip(CircleShape)
        when {
            url != null -> AsyncImage(
                modifier = baseModifier,
                model = url,
                contentDescription = node.props.string("contentDescription"),
                contentScale = ContentScale.Crop
            )
            iconName != null -> {
                val vector = iconByName(iconName)
                if (vector != null) {
                    Box(modifier = baseModifier) {
                        Icon(
                            imageVector = vector,
                            contentDescription = null,
                            modifier = Modifier
                                .size(size * 0.6f)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
    },
    "list" to { node, _, _, renderChild ->
        val spacing = node.props.spacingDp() ?: 0.dp
        val reverse = node.props.bool("reverse") ?: false
        LazyColumn(
            modifier = node.props.toModifier(),
            verticalArrangement = Arrangement.spacedBy(spacing),
            reverseLayout = reverse
        ) {
            items(node.children) { child ->
                renderChild(child)
            }
        }
    },
    "listRow" to { node, _, _, renderChild ->
        val spacing = node.props.spacingDp() ?: 0.dp
        val reverse = node.props.bool("reverse") ?: false
        LazyRow(
            modifier = node.props.toModifier(),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            reverseLayout = reverse
        ) {
            items(node.children) { child ->
                renderChild(child)
            }
        }
    },
    "listItem" to { node, _, _, renderChild ->
        val spacing = node.props.spacingDp() ?: 8.dp
        Row(
            modifier = node.props.toModifier(),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = node.props.vAlignment() ?: Alignment.CenterVertically
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "listItemM3" to { node, _, onEvent, _ ->
        val id = node.id ?: "listItem"
        val headline = node.props.string("headline") ?: ""
        val supporting = node.props.string("supporting")
        val overline = node.props.string("overline")
        val leadingIcon = node.props.string("leadingIcon")
        val trailingIcon = node.props.string("trailingIcon")
        val leadingVector = leadingIcon?.let { iconByName(it) }
        val trailingVector = trailingIcon?.let { iconByName(it) }
        ListItem(
            modifier = node.props.toModifier().clickable { onEvent(A2UiEvent(id, "click")) },
            headlineContent = { Text(headline) },
            supportingContent = if (supporting != null) {{ Text(supporting) }} else null,
            overlineContent = if (overline != null) {{ Text(overline) }} else null,
            leadingContent = if (leadingVector != null) {{ Icon(leadingVector, contentDescription = null) }} else null,
            trailingContent = if (trailingVector != null) {{ Icon(trailingVector, contentDescription = null) }} else null
        )
    }
)
