package com.example.composegenui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                    ComponentCatalogApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComponentCatalogApp() {
    var selectedCategory by remember { mutableIntStateOf(0) }
    val categories = listOf("Layout", "Input", "Navigation", "Feedback", "Media")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose GenUI Catalog") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                categories.forEachIndexed { index, category ->
                    NavigationBarItem(
                        selected = selectedCategory == index,
                        onClick = { selectedCategory = index },
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.ViewModule
                                    1 -> Icons.Default.TouchApp
                                    2 -> Icons.Default.Navigation
                                    3 -> Icons.Default.Feedback
                                    else -> Icons.Default.Image
                                },
                                contentDescription = category
                            )
                        },
                        label = { Text(category) }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when (selectedCategory) {
                0 -> LayoutDemo()
                1 -> InputDemo()
                2 -> NavigationDemo()
                3 -> FeedbackDemo()
                4 -> MediaDemo()
            }
        }
    }
}

@Composable
private fun LayoutDemo() {
    DemoSection("Layout Components") {
        A2UiDemo(LAYOUT_DEMO_JSON)
    }
}

@Composable
private fun InputDemo() {
    DemoSection("Input Components") {
        A2UiDemo(INPUT_DEMO_JSON)
    }
}

@Composable
private fun NavigationDemo() {
    DemoSection("Navigation Components") {
        A2UiDemo(NAVIGATION_DEMO_JSON)
    }
}

@Composable
private fun FeedbackDemo() {
    DemoSection("Feedback Components") {
        A2UiDemo(FEEDBACK_DEMO_JSON)
    }
}

@Composable
private fun MediaDemo() {
    DemoSection("Media Components") {
        A2UiDemo(MEDIA_DEMO_JSON)
    }
}

@Composable
private fun DemoSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        content()
    }
}

@Composable
private fun A2UiDemo(json: String) {
    val document = remember(json) { A2UiJson.decodeDocument(json) }
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

// ============================================================================
// LAYOUT DEMO
// ============================================================================
private const val LAYOUT_DEMO_JSON = """
{
  "root": {
    "type": "column",
    "props": { "spacing": 24 },
    "children": [
      {
        "type": "text",
        "props": { "text": "Column & Row", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 8 },
        "children": [
          { "type": "box", "props": { "padding": 16, "background": "#E3F2FD" }, "children": [
            { "type": "text", "props": { "text": "Box 1" } }
          ]},
          { "type": "box", "props": { "padding": 16, "background": "#F3E5F5" }, "children": [
            { "type": "text", "props": { "text": "Box 2" } }
          ]},
          { "type": "box", "props": { "padding": 16, "background": "#E8F5E9" }, "children": [
            { "type": "text", "props": { "text": "Box 3" } }
          ]}
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Cards", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 12 },
        "children": [
          {
            "type": "card",
            "props": { "padding": 16, "elevation": 4 },
            "children": [
              { "type": "text", "props": { "text": "Card", "fontWeight": "medium" } },
              { "type": "text", "props": { "text": "Default card style" } }
            ]
          },
          {
            "type": "elevatedCard",
            "props": { "padding": 16, "elevation": 8 },
            "children": [
              { "type": "text", "props": { "text": "Elevated", "fontWeight": "medium" } },
              { "type": "text", "props": { "text": "Higher elevation" } }
            ]
          },
          {
            "type": "outlinedCard",
            "props": { "padding": 16 },
            "children": [
              { "type": "text", "props": { "text": "Outlined", "fontWeight": "medium" } },
              { "type": "text", "props": { "text": "Border style" } }
            ]
          }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Surface & Spacers", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "surface",
        "props": { "elevation": 2, "padding": 16 },
        "children": [
          { "type": "text", "props": { "text": "Surface with elevation" } },
          { "type": "spacer", "props": { "height": 8 } },
          { "type": "text", "props": { "text": "Spacer above creates gap", "color": "#666666" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "List Components", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "list",
        "props": { "spacing": 4, "height": 200 },
        "children": [
          { "type": "listItemM3", "props": { "headline": "List Item 1", "supporting": "With supporting text", "leadingIcon": "star" } },
          { "type": "listItemM3", "props": { "headline": "List Item 2", "supporting": "Another item", "leadingIcon": "favorite", "trailingIcon": "arrowForward" } },
          { "type": "listItemM3", "props": { "headline": "List Item 3", "overline": "OVERLINE", "supporting": "Has overline text", "leadingIcon": "info" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Scroll Containers", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "scrollRow",
        "props": { "spacing": 8, "padding": 8 },
        "children": [
          { "type": "card", "props": { "padding": 16, "width": 120 }, "children": [{ "type": "text", "props": { "text": "Scroll 1" } }] },
          { "type": "card", "props": { "padding": 16, "width": 120 }, "children": [{ "type": "text", "props": { "text": "Scroll 2" } }] },
          { "type": "card", "props": { "padding": 16, "width": 120 }, "children": [{ "type": "text", "props": { "text": "Scroll 3" } }] },
          { "type": "card", "props": { "padding": 16, "width": 120 }, "children": [{ "type": "text", "props": { "text": "Scroll 4" } }] },
          { "type": "card", "props": { "padding": 16, "width": 120 }, "children": [{ "type": "text", "props": { "text": "Scroll 5" } }] }
        ]
      }
    ]
  }
}
"""

// ============================================================================
// INPUT DEMO
// ============================================================================
private const val INPUT_DEMO_JSON = """
{
  "root": {
    "type": "column",
    "props": { "spacing": 24 },
    "children": [
      {
        "type": "text",
        "props": { "text": "Buttons", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 8 },
        "children": [
          { "type": "button", "props": { "label": "Filled" } },
          { "type": "button", "props": { "label": "Outlined", "variant": "outlined" } },
          { "type": "button", "props": { "label": "Text", "variant": "text" } }
        ]
      },
      {
        "type": "row",
        "props": { "spacing": 8 },
        "children": [
          { "type": "elevatedButton", "props": { "label": "Elevated" } },
          { "type": "tonalButton", "props": { "label": "Tonal" } },
          { "type": "iconButton", "props": { "name": "favorite" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Text Fields", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "column",
        "props": { "spacing": 12 },
        "children": [
          { "type": "textfield", "id": "outlined", "props": { "label": "Outlined TextField", "placeholder": "Enter text..." } },
          { "type": "textfield", "id": "filled", "props": { "label": "Filled TextField", "variant": "filled" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Selection Controls", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "column",
        "props": { "spacing": 8 },
        "children": [
          {
            "type": "row",
            "props": { "spacing": 8, "verticalAlignment": "center" },
            "children": [
              { "type": "checkbox", "id": "check1" },
              { "type": "text", "props": { "text": "Checkbox" } },
              { "type": "triStateCheckbox", "id": "tri", "props": { "state": "indeterminate" } },
              { "type": "text", "props": { "text": "Tri-state" } }
            ]
          },
          {
            "type": "row",
            "props": { "spacing": 8, "verticalAlignment": "center" },
            "children": [
              { "type": "switch", "id": "switch1" },
              { "type": "text", "props": { "text": "Switch" } }
            ]
          },
          {
            "type": "row",
            "props": { "spacing": 16, "verticalAlignment": "center" },
            "children": [
              { "type": "radio", "props": { "group": "demo", "value": "a", "selectedValue": "a" } },
              { "type": "text", "props": { "text": "Option A" } },
              { "type": "radio", "props": { "group": "demo", "value": "b", "selectedValue": "a" } },
              { "type": "text", "props": { "text": "Option B" } }
            ]
          }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Sliders & Steppers", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "column",
        "props": { "spacing": 12 },
        "children": [
          { "type": "slider", "id": "slider1", "props": { "value": 0.5 } },
          { "type": "rangeSlider", "id": "range1", "props": { "start": 0.2, "end": 0.8 } },
          { "type": "stepper", "id": "stepper1", "props": { "value": 5, "min": 0, "max": 10 } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Chips", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 8 },
        "children": [
          { "type": "chip", "props": { "label": "Assist", "icon": "star" } },
          { "type": "filterChip", "props": { "label": "Filter", "selected": true } },
          { "type": "inputChip", "props": { "label": "Input", "icon": "person" } },
          { "type": "suggestionChip", "props": { "label": "Suggest" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Segmented Button", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "segmentedButton",
        "id": "segment1",
        "props": { "selectedIndex": 1 },
        "children": [
          { "type": "segment", "props": { "label": "Day" } },
          { "type": "segment", "props": { "label": "Week" } },
          { "type": "segment", "props": { "label": "Month" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Search & Dropdown", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "column",
        "props": { "spacing": 12 },
        "children": [
          { "type": "searchBar", "id": "search1", "props": { "placeholder": "Search..." } }
        ]
      }
    ]
  }
}
"""

// ============================================================================
// NAVIGATION DEMO
// ============================================================================
private const val NAVIGATION_DEMO_JSON = """
{
  "root": {
    "type": "column",
    "props": { "spacing": 24 },
    "children": [
      {
        "type": "text",
        "props": { "text": "Top App Bars", "fontWeight": "bold", "fontSize": 18 }
      },
      { "type": "topAppBar", "props": { "title": "Standard", "navIcon": "menu" } },
      { "type": "spacer", "props": { "height": 8 } },
      { "type": "centerTopAppBar", "props": { "title": "Center Aligned", "navIcon": "back" } },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Tabs", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "tabs",
        "props": { "selectedIndex": 0 },
        "children": [
          { "type": "tab", "props": { "label": "Photos" } },
          { "type": "tab", "props": { "label": "Videos" } },
          { "type": "tab", "props": { "label": "Audio" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Navigation Bar", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "navigationBar",
        "children": [
          { "type": "navItem", "props": { "label": "Home", "icon": "home", "selected": true } },
          { "type": "navItem", "props": { "label": "Search", "icon": "search" } },
          { "type": "navItem", "props": { "label": "Profile", "icon": "person" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Navigation Rail", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 16 },
        "children": [
          {
            "type": "navigationRail",
            "props": { "height": 200 },
            "children": [
              { "type": "railItem", "props": { "label": "Home", "icon": "home", "selected": true } },
              { "type": "railItem", "props": { "label": "Explore", "icon": "search" } },
              { "type": "railItem", "props": { "label": "Settings", "icon": "settings" } }
            ]
          },
          {
            "type": "column",
            "props": { "spacing": 8, "padding": 16 },
            "children": [
              { "type": "text", "props": { "text": "Navigation Rail is ideal for", "fontWeight": "medium" } },
              { "type": "text", "props": { "text": "tablets and larger screens" } }
            ]
          }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Menus", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 8 },
        "children": [
          { "type": "text", "props": { "text": "Menu (expandable):" } },
          {
            "type": "menu",
            "props": { "expanded": false },
            "children": [
              { "type": "menuItem", "props": { "label": "Edit" } },
              { "type": "menuItem", "props": { "label": "Delete" } },
              { "type": "menuItem", "props": { "label": "Share" } }
            ]
          }
        ]
      }
    ]
  }
}
"""

// ============================================================================
// FEEDBACK DEMO
// ============================================================================
private const val FEEDBACK_DEMO_JSON = """
{
  "root": {
    "type": "column",
    "props": { "spacing": 24 },
    "children": [
      {
        "type": "text",
        "props": { "text": "Progress Indicators", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 24, "verticalAlignment": "center" },
        "children": [
          { "type": "progress", "props": { "variant": "circular" } },
          { "type": "progress", "props": { "variant": "circular", "value": 0.7 } },
          {
            "type": "column",
            "props": { "spacing": 8, "fill": "width" },
            "children": [
              { "type": "progress", "props": { "variant": "linear" } },
              { "type": "progress", "props": { "variant": "linear", "value": 0.5 } }
            ]
          }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Snackbar", "fontWeight": "bold", "fontSize": 18 }
      },
      { "type": "snackbar", "props": { "text": "Message sent successfully", "actionLabel": "Undo" } },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Banner", "fontWeight": "bold", "fontSize": 18 }
      },
      { "type": "banner", "props": { "text": "Your subscription is expiring soon", "icon": "warning", "actionLabel": "Renew", "dismissLabel": "Later" } },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Badges", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 24, "verticalAlignment": "center" },
        "children": [
          {
            "type": "badge",
            "props": { "label": "3" },
            "children": [{ "type": "icon", "props": { "name": "notifications" } }]
          },
          {
            "type": "badge",
            "props": { "label": "99+" },
            "children": [{ "type": "icon", "props": { "name": "email" } }]
          },
          {
            "type": "badge",
            "children": [{ "type": "icon", "props": { "name": "favorite" } }]
          }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Tooltips", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 16 },
        "children": [
          {
            "type": "tooltip",
            "props": { "text": "Add new item" },
            "children": [{ "type": "iconButton", "props": { "name": "add" } }]
          },
          {
            "type": "tooltip",
            "props": { "text": "Delete selected" },
            "children": [{ "type": "iconButton", "props": { "name": "delete" } }]
          }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "FAB Variants", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 16, "verticalAlignment": "center" },
        "children": [
          { "type": "fab", "props": { "icon": "add", "variant": "small" } },
          { "type": "fab", "props": { "icon": "edit" } },
          { "type": "fab", "props": { "icon": "add", "variant": "large" } },
          { "type": "fab", "props": { "icon": "create", "label": "Compose", "variant": "extended" } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Dialog", "fontWeight": "bold", "fontSize": 18 }
      },
      { "type": "text", "props": { "text": "(Dialogs appear as overlays when triggered)", "color": "#666666" } }
    ]
  }
}
"""

// ============================================================================
// MEDIA DEMO
// ============================================================================
private const val MEDIA_DEMO_JSON = """
{
  "root": {
    "type": "column",
    "props": { "spacing": 24 },
    "children": [
      {
        "type": "text",
        "props": { "text": "Images", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "image",
        "props": {
          "url": "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800",
          "height": 200,
          "fill": "width",
          "contentScale": "crop"
        }
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Icons", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 16 },
        "children": [
          { "type": "icon", "props": { "name": "home" } },
          { "type": "icon", "props": { "name": "favorite", "color": "#E91E63" } },
          { "type": "icon", "props": { "name": "star", "color": "#FFC107" } },
          { "type": "icon", "props": { "name": "settings", "color": "#607D8B" } },
          { "type": "icon", "props": { "name": "notifications", "color": "#2196F3" } },
          { "type": "icon", "props": { "name": "search" } },
          { "type": "icon", "props": { "name": "email", "color": "#4CAF50" } }
        ]
      },
      {
        "type": "text",
        "props": { "text": "45+ icons available", "color": "#666666" }
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Avatars", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "row",
        "props": { "spacing": 16, "verticalAlignment": "center" },
        "children": [
          { "type": "avatar", "props": { "url": "https://i.pravatar.cc/100?img=1", "size": 40 } },
          { "type": "avatar", "props": { "url": "https://i.pravatar.cc/100?img=2", "size": 56 } },
          { "type": "avatar", "props": { "url": "https://i.pravatar.cc/100?img=3", "size": 72 } },
          { "type": "avatar", "props": { "icon": "person", "size": 48 } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Text Styling", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "column",
        "props": { "spacing": 8 },
        "children": [
          { "type": "text", "props": { "text": "Bold Text", "fontWeight": "bold" } },
          { "type": "text", "props": { "text": "Italic Text", "fontStyle": "italic" } },
          { "type": "text", "props": { "text": "Large Text", "fontSize": 24 } },
          { "type": "text", "props": { "text": "Colored Text", "color": "#6200EE" } },
          { "type": "text", "props": { "text": "SPACED TEXT", "letterSpacing": 4 } }
        ]
      },
      { "type": "divider" },
      {
        "type": "text",
        "props": { "text": "Pager / Carousel", "fontWeight": "bold", "fontSize": 18 }
      },
      {
        "type": "horizontalPager",
        "props": { "height": 150 },
        "children": [
          {
            "type": "page",
            "children": [{
              "type": "card",
              "props": { "padding": 24, "fill": "both", "background": "#E3F2FD" },
              "children": [{ "type": "text", "props": { "text": "Page 1", "fontSize": 24 } }]
            }]
          },
          {
            "type": "page",
            "children": [{
              "type": "card",
              "props": { "padding": 24, "fill": "both", "background": "#F3E5F5" },
              "children": [{ "type": "text", "props": { "text": "Page 2", "fontSize": 24 } }]
            }]
          },
          {
            "type": "page",
            "children": [{
              "type": "card",
              "props": { "padding": 24, "fill": "both", "background": "#E8F5E9" },
              "children": [{ "type": "text", "props": { "text": "Page 3", "fontSize": 24 } }]
            }]
          }
        ]
      },
      { "type": "text", "props": { "text": "← Swipe to navigate →", "textAlign": "center", "color": "#666666" } }
    ]
  }
}
"""
