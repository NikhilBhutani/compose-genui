# Compose GenUI (A2UI-aligned)

Compose GenUI is an experimental Jetpack Compose SDK that renders **A2UI (Agent-to-User Interface)** JSON into native Compose UIs. It provides a safe, catalog-driven rendering layer and a session loop for integrating AI/agent backends. The goal is to be a Compose-native counterpart to Flutter GenUI while staying compatible with the broader A2UI ecosystem as it evolves.

Repository: https://github.com/NikhilBhutani/compose-genui

## Status
Early scaffolding and demo. The data model and renderer will evolve as the A2UI spec stabilizes.

## Key features
- A2UI JSON document model and decoder.
- Catalog-based rendering (only known components are allowed).
- Composable renderer that maps JSON → Compose UI trees.
- Session loop to handle user events and trigger AI/agent updates.
- **LLM connectors** for Anthropic (Claude), OpenAI (GPT), and Google (Gemini).
- **Schema validation** with strict mode, structural checks, and value validation.
- **55+ Material 3 components** out of the box.

## Architecture
At a high level:
1. User input produces an `A2UiEvent`.
2. A content generator (LLM/agent) produces updated A2UI JSON.
3. The renderer maps A2UI nodes to your registered composables.
4. State updates trigger Compose recomposition.

## Built-in components (current)
- Layout: `column`, `row`, `box`, `surface`, `card`, `elevatedCard`, `outlinedCard`, `spacer`, `divider`, `list`, `listRow`, `listItem`, `listItemM3`, `scaffold`, `scrollColumn`, `scrollRow`
- Inputs: `textfield`, `button`, `elevatedButton`, `tonalButton`, `iconButton`, `checkbox`, `triStateCheckbox`, `radio`, `switch`, `slider`, `rangeSlider`, `stepper`, `chip`, `filterChip`, `inputChip`, `suggestionChip`, `progress`, `segmentedButton`, `segment`, `searchBar`, `dropdown`, `option`, `datePicker`, `timePicker`
- Navigation: `topAppBar`, `centerTopAppBar`, `mediumTopAppBar`, `largeTopAppBar`, `bottomAppBar`, `navigationBar`, `navItem`, `navigationRail`, `railItem`, `navigationDrawer`, `drawerItem`, `tabs`, `tab`, `menu`, `menuItem`, `dialog`, `bottomSheet`
- Paging: `horizontalPager`, `verticalPager`, `page`
- Text: `text`
- Media: `image`, `icon`, `avatar`
- Feedback: `snackbar`, `badge`, `fab`, `banner`, `tooltip`, `richTooltip`
- Gestures: `swipeToDismiss`

## Common style props (selected)
- Layout: `padding`, `spacing`, `fill`, `width`, `height`, `size`, `reverse`
- Buttons: `variant` (filled|outlined|text), `label`, `enabled`
- TextField: `variant` (outlined|filled), `placeholder`, `singleLine`
- FAB: `variant` (regular|small|large|extended), `icon`, `label`
- Tabs: `selectedIndex`
- Navigation: `navIcon`, `selected`
- Progress: `variant` (circular|linear), `value`
- BottomSheet: `visible`
- SearchBar: `placeholder`, `active`
- SegmentedButton: `selectedIndex` (children are `segment` with `label`)
- Dropdown: `label`, `value`, `expanded` (children are `option` with `label`, `value`)
- ListItemM3: `headline`, `supporting`, `overline`, `leadingIcon`, `trailingIcon`
- Pager: `initialPage` (children are `page` components)
- Banner: `text`, `icon`, `actionLabel`, `dismissLabel`
- Tooltip: `text` (plain), or `title`, `text`, `actionLabel` (rich)
- DatePicker: `visible`
- TimePicker: `hour`, `minute`, `is24Hour`
- SwipeToDismiss: `dismissBackground`
- Shape: `cornerRadius`, `borderWidth`, `borderColor`
- Surface: `elevation`
- Text: `fontSize`, `fontWeight`, `fontStyle`, `textAlign`, `lineHeight`, `letterSpacing`, `color`

These live in `defaultA2UiCatalog()` and are intentionally minimal for safety.

## Available icons
The following icon names are supported in `icon`, `iconButton`, `fab`, and other icon-accepting components:
- Navigation: `home`, `menu`, `back`, `forward`, `arrowUp`, `arrowDown`, `arrowLeft`, `arrowRight`, `close`, `more`
- Actions: `add`, `edit`, `delete`, `search`, `refresh`, `share`, `send`, `play`, `check`, `done`, `clear`, `create`, `build`
- Status: `info`, `warning`, `notifications`, `star`, `favorite`, `favoriteBorder`, `like`
- People: `person`, `account`, `face`
- Communication: `email`, `call`, `phone`
- Places: `location`, `place`
- Other: `settings`, `lock`, `logout`, `calendar`, `list`, `cart`

## Component count
**55+ components** covering the full Material 3 design system.

## Modules
- `genui/`: library module (A2UI model, JSON parser, catalog registry, renderer, session loop).
- `app/`: sample Android app demonstrating A2UI rendering and event handling.

## Getting started
Requirements:
- Android Studio (latest stable)
- Android SDK 34+
- Kotlin 1.9.x
- JDK 17+

### Build from command line
```bash
./gradlew build        # Build the project
./gradlew test         # Run unit tests
./gradlew :app:installDebug  # Install demo app
```

### Or with Android Studio
Open the project in Android Studio, sync Gradle, and run the `app` module.

## Example
The sample app renders a simple A2UI document at runtime. See:
- `app/src/main/java/com/example/composegenui/MainActivity.kt`

## A2UI compatibility
This SDK aims to stay aligned with the A2UI format so agent outputs can be shared across ecosystems. The renderer currently supports a minimal subset of components and properties; the catalog is intentionally strict to keep generated UI safe and predictable.

## LLM Connectors

Generate A2UI interfaces using LLMs from Anthropic, OpenAI, or Google:

```kotlin
// Using Anthropic Claude
val generator = A2UiLlmGeneratorFactory.create(
    provider = LlmProvider.ANTHROPIC,
    apiKey = "your-anthropic-key"
)

// Using OpenAI GPT
val generator = A2UiLlmGeneratorFactory.create(
    provider = LlmProvider.OPENAI,
    apiKey = "your-openai-key",
    model = "gpt-4-turbo" // optional, defaults to gpt-4o
)

// Using Google Gemini
val generator = A2UiLlmGeneratorFactory.create(
    provider = LlmProvider.GEMINI,
    apiKey = "your-gemini-key"
)

// Generate UI from a prompt
val document = generator.generateFromPrompt("Create a login form with email and password")

// Or use with session for interactive updates
val session = A2UiSession(initialDocument, generator)
session.handleEvent(event) // LLM generates updated UI
```

### DSL Builder

```kotlin
val generator = a2uiLlmGenerator {
    provider = LlmProvider.ANTHROPIC
    apiKey = "your-key"
    model = "claude-3-opus-20240229"
    temperature = 0.5f
    maxTokens = 4096
    systemPrompt = "Custom system prompt..." // optional
    baseUrl = "https://proxy.example.com" // optional, for proxies
}
```

### Supported Providers

| Provider | Default Model | API Endpoint |
|----------|--------------|--------------|
| ANTHROPIC | claude-sonnet-4-20250514 | api.anthropic.com |
| OPENAI | gpt-4o | api.openai.com |
| GEMINI | gemini-2.0-flash | generativelanguage.googleapis.com |

## Schema Validation

Validate A2UI documents with comprehensive checks:

```kotlin
// Basic validation
val result = validateA2Ui(document)
if (!result.isValid) {
    result.errors.forEach { println("Error: ${it.message}") }
}

// Strict validation with all checks
val result = validateA2Ui(document, options = A2UiValidationOptions(
    strict = true,           // Warn on unknown props
    checkIds = true,         // Check for duplicate IDs
    checkStructure = true,   // Validate parent-child relationships
    checkValues = true,      // Validate prop types and ranges
    checkIcons = true        // Validate icon names
))

// Check results
result.isValid      // true if no errors
result.hasWarnings  // true if any warnings
result.errors       // list of validation errors
result.warnings     // list of validation warnings
```

### Validation Features
- **Unknown component detection**: Error on unrecognized component types
- **Required prop checking**: Error when required props are missing
- **Duplicate ID detection**: Error when same ID is used multiple times
- **Structural validation**: Warn when components are in wrong context (e.g., `segment` outside `segmentedButton`)
- **Value validation**: Warn on invalid variants, negative dimensions, out-of-range values
- **Icon validation**: Warn on unrecognized icon names
- **Strict mode**: Warn on any unknown props

## Roadmap
- ~~Expand component catalog~~ ✅ 55+ components
- ~~Add LLM connectors~~ ✅ Anthropic, OpenAI, Gemini
- ~~Enhanced schema validation~~ ✅ Structural, value, and icon checks
- Add streaming support for LLM responses
- Add more unit tests

## Contributing
Contributions are welcome. Please open an issue or PR with:
- A clear description of the change.
- Any relevant design notes or compatibility concerns.

If you add new components, include:
- JSON schema updates (if applicable).
- Catalog entry + renderer mapping.
- Demo usage in the sample app.

## Code of Conduct
TBD. A standard Code of Conduct will be added before the first release.

## Security
Please report security issues responsibly. A `SECURITY.md` will be added as the project matures.

## License
MIT License. See `LICENSE`.

## Maintainers
- Nikhil Bhutani

## Acknowledgements
Inspired by Flutter GenUI and the A2UI initiative.
