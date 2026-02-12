package com.nikhilbhutani.composegenui.demo.presentation.settings

import androidx.lifecycle.ViewModel
import com.nikhilbhutani.composegenui.demo.data.DemoSettings
import com.nikhilbhutani.composegenui.demo.data.LlmProviderOption
import com.nikhilbhutani.composegenui.demo.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    private val _settings = MutableStateFlow(repository.load())
    val settings: StateFlow<DemoSettings> = _settings.asStateFlow()

    fun updateProvider(provider: LlmProviderOption) {
        val updated = _settings.value.copy(
            provider = provider,
            model = if (provider == LlmProviderOption.DEMO) "" else _settings.value.model
        )
        _settings.value = updated
        repository.save(updated)
    }

    fun updateApiKey(apiKey: String) {
        val updated = _settings.value.copy(apiKey = apiKey)
        _settings.value = updated
        repository.save(updated)
    }

    fun updateModel(model: String) {
        val updated = _settings.value.copy(model = model)
        _settings.value = updated
        repository.save(updated)
    }
}
