@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.nikhilbhutani.composegenui.builtins

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nikhilbhutani.composegenui.A2UiEvent
import com.nikhilbhutani.composegenui.A2UiRenderer
import com.nikhilbhutani.composegenui.bool
import com.nikhilbhutani.composegenui.string
import com.nikhilbhutani.composegenui.toModifier
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal fun chipBuiltins(): Map<String, A2UiRenderer> = mapOf(
    "chip" to { node, _, onEvent, _ ->
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
    "filterChip" to { node, _, onEvent, _ ->
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
    "inputChip" to @Composable { node, _, onEvent, _ ->
        val id = node.id ?: "inputChip"
        val label = node.props.string("label") ?: "Input"
        val selected = node.props.bool("selected") ?: false
        val iconName = node.props.string("icon")
        val iconVector = iconName?.let { iconByName(it) }
        InputChip(
            modifier = node.props.toModifier(),
            selected = selected,
            onClick = { onEvent(A2UiEvent(id, "click")) },
            label = { Text(label) },
            leadingIcon = if (iconVector != null) {
                { Icon(imageVector = iconVector, contentDescription = null) }
            } else null,
            trailingIcon = {
                IconButton(onClick = { onEvent(A2UiEvent(id, "dismiss")) }) {
                    Icon(Icons.Filled.Close, contentDescription = "Remove")
                }
            }
        )
    },
    "suggestionChip" to { node, _, onEvent, _ ->
        val id = node.id ?: "suggestionChip"
        val label = node.props.string("label") ?: "Suggestion"
        val iconName = node.props.string("icon")
        val iconVector = iconName?.let { iconByName(it) }
        SuggestionChip(
            modifier = node.props.toModifier(),
            onClick = { onEvent(A2UiEvent(id, "click")) },
            label = { Text(label) },
            icon = if (iconVector != null) {
                { Icon(imageVector = iconVector, contentDescription = null) }
            } else null
        )
    },
    "searchBar" to @Composable { node, state, onEvent, renderChild ->
        val id = node.id ?: "searchBar"
        val placeholder = node.props.string("placeholder") ?: "Search"
        val query = state.string(id) ?: ""
        val active = node.props.bool("active") ?: false
        val onQueryChange: (String) -> Unit = { value ->
            onEvent(A2UiEvent(id, "input", JsonObject(mapOf("value" to JsonPrimitive(value)))))
        }
        val onSearch: (String) -> Unit = { value ->
            onEvent(A2UiEvent(id, "search", JsonObject(mapOf("query" to JsonPrimitive(value)))))
        }
        val onActiveChange: (Boolean) -> Unit = { isActive ->
            onEvent(A2UiEvent(id, "activeChange", JsonObject(mapOf("active" to JsonPrimitive(isActive)))))
        }
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = onSearch,
                    expanded = active,
                    onExpandedChange = onActiveChange,
                    placeholder = { Text(placeholder) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
                )
            },
            expanded = active,
            onExpandedChange = onActiveChange,
            modifier = node.props.toModifier(),
        ) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "dropdown" to @Composable { node, state, onEvent, _ ->
        val id = node.id ?: "dropdown"
        val label = node.props.string("label") ?: ""
        val expanded = node.props.bool("expanded") ?: false
        val selectedValue = state.string(id) ?: node.props.string("value") ?: ""
        ExposedDropdownMenuBox(
            modifier = node.props.toModifier(),
            expanded = expanded,
            onExpandedChange = { isExpanded ->
                onEvent(A2UiEvent(id, "expandedChange", JsonObject(mapOf("expanded" to JsonPrimitive(isExpanded)))))
            }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                value = selectedValue,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onEvent(A2UiEvent(id, "dismiss")) }
            ) {
                node.children.forEach { child ->
                    val optionLabel = child.props.string("label") ?: ""
                    val optionValue = child.props.string("value") ?: optionLabel
                    DropdownMenuItem(
                        text = { Text(optionLabel) },
                        onClick = {
                            onEvent(A2UiEvent(id, "select", JsonObject(mapOf("value" to JsonPrimitive(optionValue)))))
                        }
                    )
                }
            }
        }
    },
    "option" to { _, _, _, _ ->
        // Placeholder - rendering handled by dropdown parent
    }
)
