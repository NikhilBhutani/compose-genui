package com.example.composegenui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.genui.A2UiCatalog
import com.example.genui.A2UiEvent
import com.example.genui.A2UiJson
import com.example.genui.A2UiNode
import com.example.genui.A2UiRender
import com.example.genui.A2UiState
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GenUiDemo()
                }
            }
        }
    }
}

@Composable
private fun GenUiDemo() {
    val document = remember { A2UiJson.decodeDocument(SAMPLE_DOC_JSON) }
    val state = remember { mutableStateMapOf<String, JsonElement>() }
    val uiState by remember { derivedStateOf { A2UiState(state.toMap()) } }

    A2UiRender(
        document = document,
        catalog = demoCatalog(),
        state = uiState,
        onEvent = { event ->
            when (event.action) {
                "input" -> {
                    val value = event.payload["value"]
                    if (value != null) {
                        state[event.nodeId] = value
                    }
                }
                else -> Unit
            }
        }
    )
}

private fun demoCatalog(): A2UiCatalog = object : A2UiCatalog {
    @Composable
    override fun Render(
        node: A2UiNode,
        state: A2UiState,
        onEvent: (A2UiEvent) -> Unit,
        renderChild: @Composable (A2UiNode) -> Unit
    ) {
        when (node.type) {
            "column" -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    node.children.forEach(renderChild)
                }
            }
            "text" -> {
                Text(text = node.props.string("text") ?: "")
            }
            "button" -> {
                val label = node.props.string("label") ?: "Action"
                Button(onClick = {
                    val id = node.id ?: "button"
                    onEvent(A2UiEvent(id, "click"))
                }) {
                    Text(text = label)
                }
            }
            "textfield" -> {
                val id = node.id ?: "input"
                val label = node.props.string("label") ?: ""
                val current = state.value(id)?.jsonPrimitive?.content ?: ""
                OutlinedTextField(
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
            }
            else -> {
                node.children.forEach(renderChild)
            }
        }
    }
}

private fun JsonObject.string(key: String): String? =
    this[key]?.jsonPrimitive?.contentOrNull

private const val SAMPLE_DOC_JSON = """
{
  "root": {
    "type": "column",
    "children": [
      { "type": "text", "props": { "text": "Compose GenUI (A2UI-aligned)" } },
      { "type": "textfield", "id": "name", "props": { "label": "Your name" } },
      { "type": "button", "id": "submit", "props": { "label": "Submit" } }
    ]
  }
}
"""
