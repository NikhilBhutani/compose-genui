package com.nikhilbhutani.composegenui.demo.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(load())
    val settings: Flow<DemoSettings> = _settings.asStateFlow()

    fun load(): DemoSettings {
        val providerName = prefs.getString(KEY_PROVIDER, LlmProviderOption.DEMO.name) ?: LlmProviderOption.DEMO.name
        val apiKey = prefs.getString(KEY_API_KEY, "") ?: ""
        val model = prefs.getString(KEY_MODEL, "") ?: ""
        return DemoSettings(
            provider = LlmProviderOption.fromName(providerName),
            apiKey = apiKey,
            model = model
        )
    }

    fun save(settings: DemoSettings) {
        prefs.edit()
            .putString(KEY_PROVIDER, settings.provider.name)
            .putString(KEY_API_KEY, settings.apiKey)
            .putString(KEY_MODEL, settings.model)
            .apply()
        _settings.value = settings
    }

    companion object {
        private const val PREFS_NAME = "genui_demo_settings"
        private const val KEY_PROVIDER = "provider"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_MODEL = "model"
    }
}
