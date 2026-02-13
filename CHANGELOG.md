# Changelog

## 0.1.0 (2026-02-13)

Initial release.

- 60+ Material 3 built-in components (layout, input, navigation, feedback, media, chips, pagers, pickers)
- `GenUiConversation` orchestration facade with agentic loop, conversation history, and state management
- `GenUiSurface` Composable binding that observes conversation state and renders native UI
- `GenUiCatalog` unified schema and renderer registry
- `GenUiContentGenerator` pluggable LLM interface
- Built-in content generators: `AnthropicContentGenerator`, `OpenAiContentGenerator`, `GeminiContentGenerator`
- `FirebaseAiContentGenerator` for Firebase AI Logic SDK (compileOnly — add Firebase dependency in your app)
- A2UI JSON parsing, schema validation, and error boundaries
- User action capture and context propagation
