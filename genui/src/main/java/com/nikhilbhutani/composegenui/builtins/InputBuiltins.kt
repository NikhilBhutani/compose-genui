@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.nikhilbhutani.composegenui.builtins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nikhilbhutani.composegenui.A2UiEvent
import com.nikhilbhutani.composegenui.A2UiRenderer
import com.nikhilbhutani.composegenui.bool
import com.nikhilbhutani.composegenui.color
import com.nikhilbhutani.composegenui.float
import com.nikhilbhutani.composegenui.int
import com.nikhilbhutani.composegenui.string
import com.nikhilbhutani.composegenui.toModifier
import com.nikhilbhutani.composegenui.valueOrNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

internal fun inputBuiltins(): Map<String, A2UiRenderer> = mapOf(
    "textfield" to { node, state, onEvent, _ ->
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
    "button" to { node, _, onEvent, _ ->
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
    "elevatedButton" to { node, _, onEvent, _ ->
        val label = node.props.string("label") ?: "Action"
        val enabled = node.props.bool("enabled") ?: true
        ElevatedButton(
            modifier = node.props.toModifier(),
            enabled = enabled,
            onClick = {
                val id = node.id ?: "elevatedButton"
                onEvent(A2UiEvent(id, "click"))
            }
        ) { Text(label) }
    },
    "tonalButton" to { node, _, onEvent, _ ->
        val label = node.props.string("label") ?: "Action"
        val enabled = node.props.bool("enabled") ?: true
        FilledTonalButton(
            modifier = node.props.toModifier(),
            enabled = enabled,
            onClick = {
                val id = node.id ?: "tonalButton"
                onEvent(A2UiEvent(id, "click"))
            }
        ) { Text(label) }
    },
    "textButton" to { node, _, onEvent, _ ->
        val label = node.props.string("label") ?: "Action"
        val enabled = node.props.bool("enabled") ?: true
        TextButton(
            modifier = node.props.toModifier(),
            enabled = enabled,
            onClick = {
                val id = node.id ?: "textButton"
                onEvent(A2UiEvent(id, "click"))
            }
        ) { Text(label) }
    },
    "outlinedButton" to { node, _, onEvent, _ ->
        val label = node.props.string("label") ?: "Action"
        val enabled = node.props.bool("enabled") ?: true
        OutlinedButton(
            modifier = node.props.toModifier(),
            enabled = enabled,
            onClick = {
                val id = node.id ?: "outlinedButton"
                onEvent(A2UiEvent(id, "click"))
            }
        ) { Text(label) }
    },
    "iconButton" to { node, _, onEvent, _ ->
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
    "filledIconButton" to { node, _, onEvent, _ ->
        val name = node.props.string("name") ?: "info"
        val vector = iconByName(name)
        if (vector != null) {
            val enabled = node.props.bool("enabled") ?: true
            val tint = node.props.color("color") ?: Color.Unspecified
            FilledIconButton(
                modifier = node.props.toModifier(),
                enabled = enabled,
                onClick = {
                    val id = node.id ?: "filledIconButton"
                    onEvent(A2UiEvent(id, "click"))
                }
            ) {
                Icon(imageVector = vector, contentDescription = node.props.string("contentDescription"), tint = tint)
            }
        }
    },
    "outlinedIconButton" to { node, _, onEvent, _ ->
        val name = node.props.string("name") ?: "info"
        val vector = iconByName(name)
        if (vector != null) {
            val enabled = node.props.bool("enabled") ?: true
            val tint = node.props.color("color") ?: Color.Unspecified
            OutlinedIconButton(
                modifier = node.props.toModifier(),
                enabled = enabled,
                onClick = {
                    val id = node.id ?: "outlinedIconButton"
                    onEvent(A2UiEvent(id, "click"))
                }
            ) {
                Icon(imageVector = vector, contentDescription = node.props.string("contentDescription"), tint = tint)
            }
        }
    },
    "iconToggleButton" to { node, state, onEvent, _ ->
        val id = node.id ?: "iconToggle"
        val name = node.props.string("name") ?: "favorite"
        val vector = iconByName(name)
        if (vector != null) {
            val enabled = node.props.bool("enabled") ?: true
            val checked = state.valueOrNull(id)?.jsonPrimitive?.booleanOrNull
                ?: node.props.bool("checked")
                ?: false
            IconToggleButton(
                modifier = node.props.toModifier(),
                checked = checked,
                enabled = enabled,
                onCheckedChange = { value ->
                    onEvent(A2UiEvent(id, "input", JsonObject(mapOf("value" to JsonPrimitive(value)))))
                }
            ) {
                Icon(imageVector = vector, contentDescription = node.props.string("contentDescription"))
            }
        }
    },
    "checkbox" to { node, state, onEvent, _ ->
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
                onEvent(A2UiEvent(id, "input", JsonObject(mapOf("value" to JsonPrimitive(value)))))
            }
        )
    },
    "triStateCheckbox" to { node, state, onEvent, _ ->
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
                onEvent(A2UiEvent(id, "input", JsonObject(mapOf("value" to JsonPrimitive(next)))))
            }
        )
    },
    "radio" to { node, state, onEvent, _ ->
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
                onEvent(A2UiEvent(group, "input", JsonObject(mapOf("value" to JsonPrimitive(value)))))
            }
        )
    },
    "switch" to { node, state, onEvent, _ ->
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
                onEvent(A2UiEvent(id, "input", JsonObject(mapOf("value" to JsonPrimitive(value)))))
            }
        )
    },
    "slider" to { node, state, onEvent, _ ->
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
                onEvent(A2UiEvent(id, "input", JsonObject(mapOf("value" to JsonPrimitive(value)))))
            }
        )
    },
    "rangeSlider" to { node, _, onEvent, _ ->
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
    "stepper" to { node, state, onEvent, _ ->
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
    "segmentedButton" to @Composable { node, state, onEvent, _ ->
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
                        onEvent(A2UiEvent(id, "select", JsonObject(mapOf("index" to JsonPrimitive(index)))))
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                ) {
                    Text(label)
                }
            }
        }
    },
    "segment" to { _, _, _, _ ->
        // Placeholder - rendering handled by segmentedButton parent
    }
)
