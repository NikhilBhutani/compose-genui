@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.nikhilbhutani.composegenui.builtins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nikhilbhutani.composegenui.A2UiEvent
import com.nikhilbhutani.composegenui.A2UiRenderer
import com.nikhilbhutani.composegenui.bool
import com.nikhilbhutani.composegenui.color
import com.nikhilbhutani.composegenui.float
import com.nikhilbhutani.composegenui.string
import com.nikhilbhutani.composegenui.toModifier
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal fun feedbackBuiltins(): Map<String, A2UiRenderer> = mapOf(
    "progress" to { node, _, _, _ ->
        val variant = node.props.string("variant") ?: "circular"
        val value = node.props.float("value")
        val progress = value?.coerceIn(0f, 1f)
        when (variant) {
            "linear" -> {
                if (progress != null) {
                    LinearProgressIndicator(modifier = node.props.toModifier(), progress = { progress })
                } else {
                    LinearProgressIndicator(modifier = node.props.toModifier())
                }
            }
            else -> {
                if (progress != null) {
                    CircularProgressIndicator(modifier = node.props.toModifier(), progress = { progress })
                } else {
                    CircularProgressIndicator(modifier = node.props.toModifier())
                }
            }
        }
    },
    "snackbar" to { node, _, onEvent, _ ->
        val text = node.props.string("text") ?: ""
        val actionLabel = node.props.string("actionLabel")
        Snackbar(
            modifier = node.props.toModifier(),
            action = if (actionLabel != null) {
                {
                    TextButton(onClick = {
                        val id = node.id ?: "snackbar"
                        onEvent(A2UiEvent(id, "action"))
                    }) { Text(actionLabel) }
                }
            } else null
        ) {
            Text(text)
        }
    },
    "badge" to { node, _, _, renderChild ->
        val label = node.props.string("label")
        BadgedBox(
            badge = {
                if (label != null) {
                    Badge { Text(label) }
                } else {
                    Badge()
                }
            }
        ) {
            if (node.children.isNotEmpty()) {
                for (child in node.children) {
                    renderChild(child)
                }
            }
        }
    },
    "fab" to { node, _, onEvent, _ ->
        val variant = node.props.string("variant") ?: "regular"
        val label = node.props.string("label")
        val iconName = node.props.string("icon") ?: "add"
        val iconVector = iconByName(iconName)
        val onClick = {
            val id = node.id ?: "fab"
            onEvent(A2UiEvent(id, "click"))
        }
        when (variant) {
            "small" -> SmallFloatingActionButton(
                modifier = node.props.toModifier(),
                onClick = onClick
            ) {
                if (iconVector != null) {
                    Icon(imageVector = iconVector, contentDescription = null)
                }
            }
            "large" -> LargeFloatingActionButton(
                modifier = node.props.toModifier(),
                onClick = onClick
            ) {
                if (iconVector != null) {
                    Icon(imageVector = iconVector, contentDescription = null, modifier = Modifier.size(36.dp))
                }
            }
            "extended" -> ExtendedFloatingActionButton(
                modifier = node.props.toModifier(),
                onClick = onClick,
                icon = {
                    if (iconVector != null) {
                        Icon(imageVector = iconVector, contentDescription = null)
                    }
                },
                text = { Text(label ?: "Action") }
            )
            else -> FloatingActionButton(
                modifier = node.props.toModifier(),
                onClick = onClick
            ) {
                if (iconVector != null) {
                    Icon(imageVector = iconVector, contentDescription = null)
                }
            }
        }
    },
    "banner" to { node, _, onEvent, _ ->
        val id = node.id ?: "banner"
        val text = node.props.string("text") ?: ""
        val actionLabel = node.props.string("actionLabel")
        val dismissLabel = node.props.string("dismissLabel")
        val iconName = node.props.string("icon")
        val iconVector = iconName?.let { iconByName(it) }
        Surface(
            modifier = node.props.toModifier().fillMaxWidth(),
            tonalElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (iconVector != null) {
                    Icon(iconVector, contentDescription = null)
                }
                Text(text, modifier = Modifier.weight(1f))
                if (dismissLabel != null) {
                    TextButton(onClick = { onEvent(A2UiEvent(id, "dismiss")) }) {
                        Text(dismissLabel)
                    }
                }
                if (actionLabel != null) {
                    TextButton(onClick = { onEvent(A2UiEvent(id, "action")) }) {
                        Text(actionLabel)
                    }
                }
            }
        }
    },
    "tooltip" to @Composable { node, _, _, renderChild ->
        val text = node.props.string("text") ?: ""
        val tooltipState = rememberTooltipState()
        TooltipBox(
            modifier = node.props.toModifier(),
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = { PlainTooltip { Text(text) } },
            state = tooltipState
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "richTooltip" to @Composable { node, _, onEvent, renderChild ->
        val id = node.id ?: "richTooltip"
        val title = node.props.string("title")
        val text = node.props.string("text") ?: ""
        val actionLabel = node.props.string("actionLabel")
        val tooltipState = rememberTooltipState(isPersistent = true)
        TooltipBox(
            modifier = node.props.toModifier(),
            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
            tooltip = {
                RichTooltip(
                    title = if (title != null) {{ Text(title) }} else null,
                    action = if (actionLabel != null) {{
                        TextButton(onClick = { onEvent(A2UiEvent(id, "action")) }) { Text(actionLabel) }
                    }} else null
                ) { Text(text) }
            },
            state = tooltipState
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "swipeToDismiss" to @Composable { node, _, onEvent, renderChild ->
        val id = node.id ?: "swipe"
        val dismissState = rememberSwipeToDismissBoxState(
            confirmValueChange = { value ->
                if (value != SwipeToDismissBoxValue.Settled) {
                    val direction = when (value) {
                        SwipeToDismissBoxValue.StartToEnd -> "start"
                        SwipeToDismissBoxValue.EndToStart -> "end"
                        else -> "none"
                    }
                    onEvent(A2UiEvent(id, "dismiss", JsonObject(mapOf("direction" to JsonPrimitive(direction)))))
                }
                true
            }
        )
        SwipeToDismissBox(
            modifier = node.props.toModifier(),
            state = dismissState,
            backgroundContent = {
                val bgColor = node.props.color("dismissBackground") ?: MaterialTheme.colorScheme.errorContainer
                Box(modifier = Modifier.fillMaxWidth().background(bgColor))
            }
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    }
)
