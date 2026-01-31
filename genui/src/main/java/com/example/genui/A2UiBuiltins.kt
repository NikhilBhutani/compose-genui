package com.example.genui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

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
            Surface(modifier = node.props.toModifier()) {
                node.children.forEach(renderChild)
            }
        },
        "divider" to { node, state, onEvent, renderChild ->
            Divider(modifier = node.props.toModifier())
        },
        "text" to { node, state, onEvent, renderChild ->
            val text = node.props.string("text") ?: ""
            Text(text = text, modifier = node.props.toModifier())
        },
        "button" to { node, state, onEvent, renderChild ->
            val label = node.props.string("label") ?: "Action"
            Button(
                modifier = node.props.toModifier(),
                onClick = {
                    val id = node.id ?: "button"
                    onEvent(A2UiEvent(id, "click"))
                }
            ) {
                Text(text = label)
            }
        },
        "textfield" to { node, state, onEvent, renderChild ->
            val id = node.id ?: "input"
            val label = node.props.string("label") ?: ""
            val current = state.string(id) ?: ""
            OutlinedTextField(
                modifier = node.props.toModifier(),
                value = current,
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
