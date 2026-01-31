package com.example.composegenui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.genui.A2UiJson
import com.example.genui.A2UiRender
import com.example.genui.A2UiState
import com.example.genui.defaultA2UiCatalog
import kotlinx.serialization.json.JsonElement

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
        catalog = defaultA2UiCatalog(),
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

private const val SAMPLE_DOC_JSON = """
{
  "root": {
    "type": "column",
    "props": { "padding": 24, "spacing": 16 },
    "children": [
      {
        "type": "topAppBar",
        "props": { "title": "Compose GenUI", "navIcon": "menu" },
        "children": [
          { "type": "iconButton", "props": { "name": "search" } },
          { "type": "iconButton", "props": { "name": "settings" } }
        ]
      },
      {
        "type": "tabs",
        "props": { "selectedIndex": 0 },
        "children": [
          { "type": "tab", "props": { "label": "Home" } },
          { "type": "tab", "props": { "label": "Explore" } },
          { "type": "tab", "props": { "label": "Profile" } }
        ]
      },
      {
        "type": "text",
        "props": {
          "text": "Compose GenUI (A2UI-aligned)",
          "fontSize": 20,
          "fontWeight": "bold",
          "letterSpacing": 0.5
        }
      },
      {
        "type": "row",
        "props": { "spacing": 12, "verticalAlignment": "center" },
        "children": [
          { "type": "textfield", "id": "name", "props": { "label": "Your name", "width": 220 } },
          { "type": "button", "id": "submit", "props": { "label": "Submit" } },
          { "type": "button", "id": "cancel", "props": { "label": "Cancel", "variant": "outlined" } },
          { "type": "iconButton", "props": { "name": "settings" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "card",
        "props": { "padding": 12, "cornerRadius": 12, "borderWidth": 1, "borderColor": "#E0E0E0" },
        "children": [
          {
            "type": "text",
            "props": { "text": "Card container", "color": "#0066FF", "fontStyle": "italic" }
          },
          { "type": "spacer", "props": { "height": 8 } },
          {
            "type": "row",
            "props": { "spacing": 12, "verticalAlignment": "center" },
            "children": [
              { "type": "checkbox", "id": "optin", "props": { "checked": true } },
              { "type": "text", "props": { "text": "Enable updates" } }
            ]
          }
        ]
      },
      {
        "type": "row",
        "props": { "spacing": 16, "verticalAlignment": "center" },
        "children": [
          { "type": "text", "props": { "text": "Notifications" } },
          { "type": "switch", "id": "notify", "props": { "checked": false } }
        ]
      },
      {
        "type": "column",
        "props": { "spacing": 8 },
        "children": [
          { "type": "text", "props": { "text": "Volume", "fontWeight": "medium" } },
          { "type": "slider", "id": "volume", "props": { "min": 0, "max": 100, "value": 30 } }
        ]
      },
      {
        "type": "row",
        "props": { "spacing": 12, "verticalAlignment": "center" },
        "children": [
          { "type": "icon", "props": { "name": "favorite", "color": "#E91E63" } },
          { "type": "text", "props": { "text": "Featured" } }
        ]
      },
      {
        "type": "image",
        "props": {
          "url": "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=800",
          "height": 160,
          "fill": "width",
          "contentScale": "crop"
        }
      },
      {
        "type": "list",
        "props": { "spacing": 8 },
        "children": [
          {
            "type": "listItem",
            "props": { "spacing": 8, "verticalAlignment": "center" },
            "children": [
              { "type": "icon", "props": { "name": "check", "color": "#4CAF50" } },
              { "type": "text", "props": { "text": "List item A" } }
            ]
          },
          {
            "type": "listItem",
            "props": { "spacing": 8, "verticalAlignment": "center" },
            "children": [
              { "type": "icon", "props": { "name": "check", "color": "#4CAF50" } },
              { "type": "text", "props": { "text": "List item B" } }
            ]
          },
          {
            "type": "listItem",
            "props": { "spacing": 8, "verticalAlignment": "center" },
            "children": [
              { "type": "icon", "props": { "name": "check", "color": "#4CAF50" } },
              { "type": "text", "props": { "text": "List item C" } }
            ]
          }
        ]
      },
      {
        "type": "listRow",
        "props": { "spacing": 12, "padding": 4 },
        "children": [
          { "type": "chip", "props": { "label": "Compose", "icon": "favorite" } },
          { "type": "chip", "props": { "label": "GenUI" } },
          { "type": "chip", "props": { "label": "A2UI" } }
        ]
      },
      {
        "type": "row",
        "props": { "spacing": 12, "verticalAlignment": "center" },
        "children": [
          { "type": "progress", "props": { "variant": "circular" } },
          { "type": "progress", "props": { "variant": "linear", "value": 0.6, "fill": "width" } }
        ]
      },
      {
        "type": "box",
        "props": { "padding": 12, "background": "#1F1F1F", "contentAlignment": "center" },
        "children": [
          { "type": "text", "props": { "text": "Box + background via modifiers", "color": "#FFFFFF" } }
        ]
      },
      { "type": "spacer", "props": { "height": 8 } },
      {
        "type": "surface",
        "children": [
          { "type": "text", "props": { "text": "Surface container" } }
        ]
      },
      {
        "type": "navigationBar",
        "children": [
          { "type": "navItem", "props": { "label": "Home", "icon": "favorite", "selected": true } },
          { "type": "navItem", "props": { "label": "Search", "icon": "search" } },
          { "type": "navItem", "props": { "label": "Settings", "icon": "settings" } }
        ]
      }
    ]
  }
}
"""
