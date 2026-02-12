package com.nikhilbhutani.composegenui.demo.presentation.templates

internal val LAYOUT_DEMO_JSON = """
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
      }
    ]
  }
}
""".trimIndent()

internal val INPUT_DEMO_JSON = """
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
      }
    ]
  }
}
""".trimIndent()

internal val LOGIN_TEMPLATE_JSON = """
{
  "root": {
    "type": "column",
    "props": { "padding": 16, "spacing": 12 },
    "children": [
      { "type": "text", "props": { "text": "Welcome Back", "fontWeight": "bold", "fontSize": 20 } },
      { "type": "textfield", "id": "email", "props": { "label": "Email", "placeholder": "you@example.com" } },
      { "type": "textfield", "id": "password", "props": { "label": "Password", "variant": "filled", "placeholder": "\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022" } },
      { "type": "button", "id": "signIn", "props": { "label": "Sign in" } },
      { "type": "text", "props": { "text": "Forgot password?", "color": "#666666" } }
    ]
  }
}
""".trimIndent()

internal val PROFILE_TEMPLATE_JSON = """
{
  "root": {
    "type": "column",
    "props": { "padding": 16, "spacing": 16 },
    "children": [
      {
        "type": "row",
        "props": { "spacing": 12, "verticalAlignment": "center" },
        "children": [
          { "type": "avatar", "props": { "url": "https://i.pravatar.cc/100?img=8", "size": 56 } },
          {
            "type": "column",
            "props": { "spacing": 4 },
            "children": [
              { "type": "text", "props": { "text": "Alex Chen", "fontWeight": "bold", "fontSize": 18 } },
              { "type": "text", "props": { "text": "Product Designer", "color": "#666666" } }
            ]
          }
        ]
      },
      {
        "type": "card",
        "props": { "padding": 16 },
        "children": [
          { "type": "text", "props": { "text": "Weekly Activity", "fontWeight": "medium" } },
          { "type": "spacer", "props": { "height": 8 } },
          { "type": "progress", "props": { "variant": "linear", "value": 0.65 } }
        ]
      },
      { "type": "button", "id": "editProfile", "props": { "label": "Edit Profile", "variant": "outlined" } }
    ]
  }
}
""".trimIndent()

internal val FALLBACK_TEMPLATE_JSON = """
{
  "root": {
    "type": "column",
    "props": { "padding": 16, "spacing": 12 },
    "children": [
      { "type": "text", "props": { "text": "Generated UI Preview", "fontWeight": "bold", "fontSize": 18 } },
      { "type": "text", "props": { "text": "Try prompts like: login screen, profile card, or settings." } },
      {
        "type": "row",
        "props": { "spacing": 8 },
        "children": [
          { "type": "chip", "props": { "label": "Login screen" } },
          { "type": "chip", "props": { "label": "Profile card" } },
          { "type": "chip", "props": { "label": "Settings" } }
        ]
      }
    ]
  }
}
""".trimIndent()
