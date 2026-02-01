package com.example.genui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
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
        "filterChip" to { node, state, onEvent, renderChild ->
            val label = node.props.string("label") ?: "Filter"
            val selected = node.props.bool("selected") ?: false
            FilterChip(
                modifier = node.props.toModifier(),
                selected = selected,
                onClick = {
                    val id = node.id ?: "filterChip"
                    onEvent(A2UiEvent(id, "click"))
                },
                label = { Text(label) }
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
        "snackbar" to { node, state, onEvent, renderChild ->
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
        "badge" to { node, state, onEvent, renderChild ->
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
                    node.children.forEach(renderChild)
                }
            }
        },
        "avatar" to { node, state, onEvent, renderChild ->
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
            val variant = node.props.string("variant") ?: "outlined"
            val placeholder = node.props.string("placeholder")
            val singleLine = node.props.bool("singleLine") ?: false
            val current = state.string(id) ?: ""
            val onChange: (String) -> Unit = { value ->
                onEvent(
                    A2UiEvent(
                        nodeId = id,
                        action = "input",
                        payload = JsonObject(mapOf("value" to JsonPrimitive(value)))
                    )
                )
            }
            if (variant == "filled") {
                TextField(
                    modifier = node.props.toModifier(),
                    value = current,
                    enabled = enabled,
                    onValueChange = onChange,
                    singleLine = singleLine,
                    label = { if (label.isNotEmpty()) Text(label) },
                    placeholder = { if (placeholder != null) Text(placeholder) }
                )
            } else {
                OutlinedTextField(
                    modifier = node.props.toModifier(),
                    value = current,
                    enabled = enabled,
                    onValueChange = onChange,
                    singleLine = singleLine,
                    label = { if (label.isNotEmpty()) Text(label) },
                    placeholder = { if (placeholder != null) Text(placeholder) }
                )
            }
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
        "triStateCheckbox" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "triState"
            val enabled = node.props.bool("enabled") ?: true
            val raw = state.valueOrNull(id)?.jsonPrimitive?.contentOrNull
                ?: node.props.string("state")
                ?: "off"
            val stateValue = when (raw) {
                "on" -> androidx.compose.ui.state.ToggleableState.On
                "indeterminate" -> androidx.compose.ui.state.ToggleableState.Indeterminate
                else -> androidx.compose.ui.state.ToggleableState.Off
            }
            TriStateCheckbox(
                modifier = node.props.toModifier(),
                state = stateValue,
                enabled = enabled,
                onClick = {
                    val next = when (stateValue) {
                        androidx.compose.ui.state.ToggleableState.Off -> "on"
                        androidx.compose.ui.state.ToggleableState.On -> "indeterminate"
                        androidx.compose.ui.state.ToggleableState.Indeterminate -> "off"
                    }
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "input",
                            payload = JsonObject(mapOf("value" to JsonPrimitive(next)))
                        )
                    )
                }
            )
        },
        "radio" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "radio"
            val enabled = node.props.bool("enabled") ?: true
            val value = node.props.string("value") ?: "value"
            val group = node.props.string("group") ?: "group"
            val selectedValue = state.valueOrNull(group)?.jsonPrimitive?.contentOrNull
                ?: node.props.string("selectedValue")
            val selected = selectedValue == value
            RadioButton(
                modifier = node.props.toModifier(),
                selected = selected,
                enabled = enabled,
                onClick = {
                    onEvent(
                        A2UiEvent(
                            nodeId = group,
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
        "rangeSlider" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "range"
            val enabled = node.props.bool("enabled") ?: true
            val min = node.props.float("min") ?: 0f
            val max = node.props.float("max") ?: 1f
            val steps = node.props.int("steps") ?: 0
            val start = node.props.float("start") ?: min
            val end = node.props.float("end") ?: max
            RangeSlider(
                modifier = node.props.toModifier(),
                enabled = enabled,
                value = start.coerceIn(min, max)..end.coerceIn(min, max),
                steps = steps,
                onValueChange = { range ->
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "input",
                            payload = JsonObject(
                                mapOf(
                                    "start" to JsonPrimitive(range.start),
                                    "end" to JsonPrimitive(range.endInclusive)
                                )
                            )
                        )
                    )
                }
            )
        },
        "stepper" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "stepper"
            val min = node.props.int("min") ?: 0
            val max = node.props.int("max") ?: 100
            val step = node.props.int("step") ?: 1
            val current = state.valueOrNull(id)?.jsonPrimitive?.intOrNull
                ?: node.props.int("value")
                ?: min
            val minusEnabled = current > min
            val plusEnabled = current < max
            Row(
                modifier = node.props.toModifier(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = {
                    val next = (current - step).coerceAtLeast(min)
                    onEvent(A2UiEvent(id, "input", JsonObject(mapOf("value" to JsonPrimitive(next)))))
                }, enabled = minusEnabled) { Text("-") }
                Text(text = current.toString())
                OutlinedButton(onClick = {
                    val next = (current + step).coerceAtMost(max)
                    onEvent(A2UiEvent(id, "input", JsonObject(mapOf("value" to JsonPrimitive(next)))))
                }, enabled = plusEnabled) { Text("+") }
            }
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
        },
        "fab" to { node, state, onEvent, renderChild ->
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
                    icon = if (iconVector != null) {
                        { Icon(imageVector = iconVector, contentDescription = null) }
                    } else null,
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
        "scaffold" to { node, state, onEvent, renderChild ->
            val topBarNode = node.children.find { it.type == "topAppBar" }
            val bottomBarNode = node.children.find { it.type == "navigationBar" }
            val fabNode = node.children.find { it.type == "fab" }
            val contentNodes = node.children.filter { 
                it.type != "topAppBar" && it.type != "navigationBar" && it.type != "fab" 
            }
            Scaffold(
                modifier = node.props.toModifier(),
                topBar = {
                    if (topBarNode != null) {
                        renderChild(topBarNode)
                    }
                },
                bottomBar = {
                    if (bottomBarNode != null) {
                        renderChild(bottomBarNode)
                    }
                },
                floatingActionButton = {
                    if (fabNode != null) {
                        renderChild(fabNode)
                    }
                }
            ) { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues)) {
                    contentNodes.forEach(renderChild)
                }
            }
        },
        "bottomSheet" to @Composable { node, state, onEvent, renderChild ->
            val id = node.id ?: "bottomSheet"
            val visible = node.props.bool("visible") ?: true
            if (visible) {
                ModalBottomSheet(
                    modifier = node.props.toModifier(),
                    onDismissRequest = {
                        onEvent(A2UiEvent(id, "dismiss"))
                    }
                ) {
                    node.children.forEach(renderChild)
                }
            }
        },
        "searchBar" to @Composable { node, state, onEvent, renderChild ->
            val id = node.id ?: "searchBar"
            val placeholder = node.props.string("placeholder") ?: "Search"
            val query = state.string(id) ?: ""
            val active = node.props.bool("active") ?: false
            DockedSearchBar(
                modifier = node.props.toModifier(),
                query = query,
                onQueryChange = { value ->
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "input",
                            payload = JsonObject(mapOf("value" to JsonPrimitive(value)))
                        )
                    )
                },
                onSearch = { value ->
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "search",
                            payload = JsonObject(mapOf("query" to JsonPrimitive(value)))
                        )
                    )
                },
                active = active,
                onActiveChange = { isActive ->
                    onEvent(
                        A2UiEvent(
                            nodeId = id,
                            action = "activeChange",
                            payload = JsonObject(mapOf("active" to JsonPrimitive(isActive)))
                        )
                    )
                },
                placeholder = { Text(placeholder) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
            ) {
                node.children.forEach(renderChild)
            }
        },
        "segmentedButton" to @Composable { node, state, onEvent, renderChild ->
            val id = node.id ?: "segmented"
            val options = node.children.mapNotNull { it.props.string("label") }
            val selectedIndex = state.valueOrNull(id)?.jsonPrimitive?.intOrNull
                ?: node.props.int("selectedIndex")
                ?: 0
            SingleChoiceSegmentedButtonRow(modifier = node.props.toModifier()) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        selected = index == selectedIndex,
                        onClick = {
                            onEvent(
                                A2UiEvent(
                                    nodeId = id,
                                    action = "select",
                                    payload = JsonObject(mapOf("index" to JsonPrimitive(index)))
                                )
                            )
                        },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                    ) {
                        Text(label)
                    }
                }
            }
        },
        "segment" to { node, state, onEvent, renderChild ->
            // Placeholder for segment children - rendering handled by segmentedButton parent
        }
    )
)

private fun iconByName(name: String): ImageVector? = when (name) {
    "account", "accountCircle" -> Icons.Filled.AccountCircle
    "add" -> Icons.Filled.Add
    "arrowBack", "back" -> Icons.Filled.ArrowBack
    "arrowForward", "forward" -> Icons.Filled.ArrowForward
    "build" -> Icons.Filled.Build
    "call" -> Icons.Filled.Call
    "check" -> Icons.Filled.Check
    "clear" -> Icons.Filled.Clear
    "close" -> Icons.Filled.Close
    "create" -> Icons.Filled.Create
    "dateRange", "calendar" -> Icons.Filled.DateRange
    "delete" -> Icons.Filled.Delete
    "done" -> Icons.Filled.Done
    "edit" -> Icons.Filled.Edit
    "email", "mail" -> Icons.Filled.Email
    "exit", "logout" -> Icons.Filled.ExitToApp
    "face" -> Icons.Filled.Face
    "favorite" -> Icons.Filled.Favorite
    "favoriteBorder" -> Icons.Filled.FavoriteBorder
    "home" -> Icons.Filled.Home
    "info" -> Icons.Filled.Info
    "keyboardArrowDown", "arrowDown" -> Icons.Filled.KeyboardArrowDown
    "keyboardArrowLeft", "arrowLeft" -> Icons.Filled.KeyboardArrowLeft
    "keyboardArrowRight", "arrowRight" -> Icons.Filled.KeyboardArrowRight
    "keyboardArrowUp", "arrowUp" -> Icons.Filled.KeyboardArrowUp
    "list" -> Icons.Filled.List
    "location", "locationOn" -> Icons.Filled.LocationOn
    "lock" -> Icons.Filled.Lock
    "menu" -> Icons.Filled.Menu
    "moreVert", "more" -> Icons.Filled.MoreVert
    "notifications", "notification" -> Icons.Filled.Notifications
    "person" -> Icons.Filled.Person
    "phone" -> Icons.Filled.Phone
    "place" -> Icons.Filled.Place
    "play", "playArrow" -> Icons.Filled.PlayArrow
    "refresh" -> Icons.Filled.Refresh
    "search" -> Icons.Filled.Search
    "send" -> Icons.Filled.Send
    "settings" -> Icons.Filled.Settings
    "share" -> Icons.Filled.Share
    "shoppingCart", "cart" -> Icons.Filled.ShoppingCart
    "star" -> Icons.Filled.Star
    "thumbUp", "like" -> Icons.Filled.ThumbUp
    "warning" -> Icons.Filled.Warning
    else -> null
}
