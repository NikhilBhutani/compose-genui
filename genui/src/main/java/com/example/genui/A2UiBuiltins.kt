package com.example.genui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

fun defaultA2UiCatalog(): A2UiCatalog = A2UiCatalogRegistry(
    mapOf(
        "column" to { node, state, onEvent, renderChild ->
            val props = node.props
            val modifier = props.toModifier()
            val spacing = props.spacingDp()
            val arrangement = spacing?.let { Arrangement.spacedBy(it) } ?: Arrangement.Top
            val hAlign = props.hAlignment() ?: Alignment.Start
            Column(
                modifier = modifier,
                verticalArrangement = arrangement,
                horizontalAlignment = hAlign
            ) {
                node.children.forEach(renderChild)
            }
        },
        "row" to { node, state, onEvent, renderChild ->
            val props = node.props
            val modifier = props.toModifier()
            val spacing = props.spacingDp()
            val arrangement = spacing?.let { Arrangement.spacedBy(it) } ?: Arrangement.Start
            val vAlign = props.vAlignment() ?: Alignment.CenterVertically
            Row(
                modifier = modifier,
                horizontalArrangement = arrangement,
                verticalAlignment = vAlign
            ) {
                node.children.forEach(renderChild)
            }
        },
        "box" to { node, state, onEvent, renderChild ->
            val props = node.props
            val modifier = props.toModifier()
            val alignment = props.contentAlignment() ?: Alignment.TopStart
            Box(
                modifier = modifier,
                contentAlignment = alignment
            ) {
                node.children.forEach(renderChild)
            }
        },
        "surface" to { node, state, onEvent, renderChild ->
            val elevation = node.props.dp("elevation") ?: 0.dp
            val radius = node.props.cornerRadius()
            val shape = radius?.let { RoundedCornerShape(it) } ?: MaterialTheme.shapes.medium
            Surface(
                modifier = node.props.toModifier(),
                tonalElevation = elevation,
                shape = shape
            ) {
                node.children.forEach(renderChild)
            }
        },
        "divider" to { node, state, onEvent, renderChild ->
            Divider(modifier = node.props.toModifier())
        },
        "text" to { node, state, onEvent, renderChild ->
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
        "button" to { node, state, onEvent, renderChild ->
            val label = node.props.string("label") ?: "Action"
            val enabled = node.props.bool("enabled") ?: true
            val variant = node.props.string("variant") ?: "filled"
            val onClick = {
                val id = node.id ?: "button"
                onEvent(A2UiEvent(id, "click"))
            }
            when (variant) {
                "text" -> TextButton(modifier = node.props.toModifier(), enabled = enabled, onClick = onClick) {
                    Text(text = label)
                }
                "outlined" -> OutlinedButton(modifier = node.props.toModifier(), enabled = enabled, onClick = onClick) {
                    Text(text = label)
                }
                else -> Button(modifier = node.props.toModifier(), enabled = enabled, onClick = onClick) {
                    Text(text = label)
                }
            }
        },
        "iconButton" to { node, state, onEvent, renderChild ->
            val name = node.props.string("name") ?: "info"
            val vector = iconByName(name)
            if (vector != null) {
                val enabled = node.props.bool("enabled") ?: true
                val tint = node.props.color("color") ?: Color.Unspecified
                IconButton(
                    modifier = node.props.toModifier(),
                    enabled = enabled,
                    onClick = {
                        val id = node.id ?: "iconButton"
                        onEvent(A2UiEvent(id, "click"))
                    }
                ) {
                    Icon(
                        imageVector = vector,
                        contentDescription = node.props.string("contentDescription"),
                        tint = tint
                    )
                }
            }
        },
        "chip" to { node, state, onEvent, renderChild ->
            val label = node.props.string("label") ?: "Chip"
            val enabled = node.props.bool("enabled") ?: true
            val iconName = node.props.string("icon")
            val iconVector = iconName?.let { iconByName(it) }
            AssistChip(
                modifier = node.props.toModifier(),
                onClick = {
                    val id = node.id ?: "chip"
                    onEvent(A2UiEvent(id, "click"))
                },
                enabled = enabled,
                label = { Text(label) },
                leadingIcon = if (iconVector != null) {
                    { Icon(imageVector = iconVector, contentDescription = null) }
                } else null
            )
        },
        "progress" to { node, state, onEvent, renderChild ->
            val variant = node.props.string("variant") ?: "circular"
            val value = node.props.float("value")
            val progress = value?.coerceIn(0f, 1f)
            when (variant) {
                "linear" -> {
                    if (progress != null) {
                        LinearProgressIndicator(modifier = node.props.toModifier(), progress = progress)
                    } else {
                        LinearProgressIndicator(modifier = node.props.toModifier())
                    }
                }
                else -> {
                    if (progress != null) {
                        CircularProgressIndicator(modifier = node.props.toModifier(), progress = progress)
                    } else {
                        CircularProgressIndicator(modifier = node.props.toModifier())
                    }
                }
            }
        },
        "topAppBar" to { node, state, onEvent, renderChild ->
            val title = node.props.string("title") ?: ""
            val navIconName = node.props.string("navIcon")
            TopAppBar(
                title = { Text(title) },
                navigationIcon = if (navIconName != null) {
                    {
                        val vector = iconByName(navIconName)
                        if (vector != null) {
                            IconButton(onClick = {
                                val id = node.id ?: "topAppBar"
                                onEvent(A2UiEvent(id, "navClick"))
                            }) {
                                Icon(imageVector = vector, contentDescription = null)
                            }
                        }
                    }
                } else null,
                actions = {
                    node.children.forEach(renderChild)
                }
            )
        },
        "navigationBar" to { node, state, onEvent, renderChild ->
            NavigationBar(modifier = node.props.toModifier()) {
                node.children.forEach(renderChild)
            }
        },
        "navItem" to { node, state, onEvent, renderChild ->
            val label = node.props.string("label") ?: ""
            val selected = node.props.bool("selected") ?: false
            val iconName = node.props.string("icon")
            val iconVector = iconName?.let { iconByName(it) }
            NavigationBarItem(
                selected = selected,
                onClick = {
                    val id = node.id ?: "navItem"
                    onEvent(A2UiEvent(id, "select"))
                },
                icon = {
                    if (iconVector != null) {
                        Icon(imageVector = iconVector, contentDescription = null)
                    }
                },
                label = { Text(label) }
            )
        },
        "tabs" to { node, state, onEvent, renderChild ->
            val selectedIndex = node.props.int("selectedIndex") ?: 0
            TabRow(selectedTabIndex = selectedIndex) {
                node.children.forEachIndexed { index, child ->
                    val label = child.props.string("label") ?: "Tab"
                    Tab(
                        selected = index == selectedIndex,
                        onClick = {
                            val id = child.id ?: "tab_$index"
                            onEvent(A2UiEvent(id, "select"))
                        },
                        text = { Text(label) }
                    )
                }
            }
        },
        "menu" to { node, state, onEvent, renderChild ->
            val expanded = node.props.bool("expanded") ?: false
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    val id = node.id ?: "menu"
                    onEvent(A2UiEvent(id, "dismiss"))
                },
                modifier = node.props.toModifier()
            ) {
                node.children.forEach(renderChild)
            }
        },
        "menuItem" to { node, state, onEvent, renderChild ->
            val label = node.props.string("label") ?: "Item"
            DropdownMenuItem(
                text = { Text(label) },
                onClick = {
                    val id = node.id ?: "menuItem"
                    onEvent(A2UiEvent(id, "select"))
                }
            )
        },
        "dialog" to { node, state, onEvent, renderChild ->
            val title = node.props.string("title")
            val text = node.props.string("text")
            val confirm = node.props.string("confirmLabel") ?: "OK"
            val dismiss = node.props.string("dismissLabel")
            AlertDialog(
                onDismissRequest = {
                    val id = node.id ?: "dialog"
                    onEvent(A2UiEvent(id, "dismiss"))
                },
                title = if (title != null) ({ Text(title) }) else null,
                text = if (text != null) ({ Text(text) }) else null,
                confirmButton = {
                    TextButton(onClick = {
                        val id = node.id ?: "dialog"
                        onEvent(A2UiEvent(id, "confirm"))
                    }) { Text(confirm) }
                },
                dismissButton = if (dismiss != null) ({
                    TextButton(onClick = {
                        val id = node.id ?: "dialog"
                        onEvent(A2UiEvent(id, "dismiss"))
                    }) { Text(dismiss) }
                }) else null
            )
        },
        "textfield" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "input"
            val label = node.props.string("label") ?: ""
            val enabled = node.props.bool("enabled") ?: true
            val current = state.string(id) ?: ""
            OutlinedTextField(
                modifier = node.props.toModifier(),
                value = current,
                enabled = enabled,
                onValueChange = { value ->
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "input",
                            payload = JsonObject(mapOf("value" to JsonPrimitive(value)))
                        )
                    )
                },
                label = { Text(label) }
            )
        },
        "checkbox" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "checkbox"
            val enabled = node.props.bool("enabled") ?: true
            val checked = state.valueOrNull(id)?.jsonPrimitive?.booleanOrNull
                ?: node.props.bool("checked")
                ?: false
            Checkbox(
                modifier = node.props.toModifier(),
                checked = checked,
                enabled = enabled,
                onCheckedChange = { value ->
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "input",
                            payload = JsonObject(mapOf("value" to JsonPrimitive(value)))
                        )
                    )
                }
            )
        },
        "switch" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "switch"
            val enabled = node.props.bool("enabled") ?: true
            val checked = state.valueOrNull(id)?.jsonPrimitive?.booleanOrNull
                ?: node.props.bool("checked")
                ?: false
            Switch(
                modifier = node.props.toModifier(),
                checked = checked,
                enabled = enabled,
                onCheckedChange = { value ->
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "input",
                            payload = JsonObject(mapOf("value" to JsonPrimitive(value)))
                        )
                    )
                }
            )
        },
        "slider" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "slider"
            val enabled = node.props.bool("enabled") ?: true
            val min = node.props.float("min") ?: 0f
            val max = node.props.float("max") ?: 1f
            val steps = node.props.int("steps") ?: 0
            val current = state.valueOrNull(id)?.jsonPrimitive?.floatOrNull
                ?: node.props.float("value")
                ?: min
            Slider(
                modifier = node.props.toModifier(),
                value = current.coerceIn(min, max),
                enabled = enabled,
                valueRange = min..max,
                steps = steps,
                onValueChange = { value ->
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "input",
                            payload = JsonObject(mapOf("value" to JsonPrimitive(value)))
                        )
                    )
                }
            )
        },
        "card" to { node, state, onEvent, renderChild ->
            val elevation = node.props.dp("elevation") ?: 0.dp
            val radius = node.props.cornerRadius()
            val shape = radius?.let { RoundedCornerShape(it) } ?: MaterialTheme.shapes.medium
            Card(
                modifier = node.props.toModifier(),
                shape = shape,
                elevation = CardDefaults.cardElevation(defaultElevation = elevation)
            ) {
                node.children.forEach(renderChild)
            }
        },
        "image" to { node, state, onEvent, renderChild ->
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
        "icon" to { node, state, onEvent, renderChild ->
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
        "list" to { node, state, onEvent, renderChild ->
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
        "listRow" to { node, state, onEvent, renderChild ->
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
        "listItem" to { node, state, onEvent, renderChild ->
            val spacing = node.props.spacingDp() ?: 8.dp
            Row(
                modifier = node.props.toModifier(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalAlignment = node.props.vAlignment() ?: Alignment.CenterVertically
            ) {
                node.children.forEach(renderChild)
            }
        },
        "spacer" to { node, state, onEvent, renderChild ->
            val width = node.props.dp("width")
            val height = node.props.dp("height")
            when {
                width != null && height != null -> Spacer(modifier = Modifier.width(width).height(height))
                width != null -> Spacer(modifier = Modifier.width(width))
                height != null -> Spacer(modifier = Modifier.height(height))
                else -> Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )
)

private fun iconByName(name: String): ImageVector? = when (name) {
    "add" -> Icons.Filled.Add
    "back" -> Icons.Filled.ArrowBack
    "check" -> Icons.Filled.Check
    "close" -> Icons.Filled.Close
    "edit" -> Icons.Filled.Edit
    "favorite" -> Icons.Filled.Favorite
    "info" -> Icons.Filled.Info
    "menu" -> Icons.Filled.Menu
    "search" -> Icons.Filled.Search
    "settings" -> Icons.Filled.Settings
    else -> null
}
