package com.nikhilbhutani.composegenui.demo.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhilbhutani.composegenui.GenUiConversation
import com.nikhilbhutani.composegenui.defaultGenUiCatalog
import com.nikhilbhutani.composegenui.demo.data.DemoSettings
import com.nikhilbhutani.composegenui.demo.domain.model.LlmConfig
import com.nikhilbhutani.composegenui.demo.domain.usecase.CreateContentGeneratorUseCase
import com.nikhilbhutani.composegenui.demo.domain.usecase.ObserveSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(val role: ChatRole, val text: String)

enum class ChatRole { User, Assistant }

class ChatViewModel(
    private val observeSettings: ObserveSettingsUseCase,
    private val createGenerator: CreateContentGeneratorUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private var currentSettings: DemoSettings = DemoSettings()

    var conversation: GenUiConversation = buildConversation(currentSettings)
        private set

    init {
        viewModelScope.launch {
            observeSettings().collect { settings ->
                if (settingsChanged(settings)) {
                    currentSettings = settings
                    conversation.reset()
                    conversation = buildConversation(settings)
                    _messages.value = emptyList()
                }
            }
        }
    }

    fun sendPrompt(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        _messages.value = _messages.value + ChatMessage(ChatRole.User, trimmed)
        conversation.sendRequest(trimmed)
    }

    fun reset() {
        conversation.reset()
        _messages.value = emptyList()
    }

    private fun buildConversation(settings: DemoSettings): GenUiConversation {
        val config = LlmConfig(
            provider = settings.provider,
            apiKey = settings.apiKey,
            model = settings.effectiveModel
        )
        val generator = createGenerator(config)
        return GenUiConversation(
            catalog = defaultGenUiCatalog(),
            contentGenerator = generator,
            onTextResponse = { text ->
                _messages.value = _messages.value + ChatMessage(ChatRole.Assistant, text)
            },
            onError = { error ->
                _messages.value = _messages.value + ChatMessage(ChatRole.Assistant, "Error: ${error.message}")
            },
            scope = viewModelScope
        )
    }

    private fun settingsChanged(new: DemoSettings): Boolean =
        new.provider != currentSettings.provider ||
                new.apiKey != currentSettings.apiKey ||
                new.effectiveModel != currentSettings.effectiveModel
}
