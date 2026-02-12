package com.nikhilbhutani.composegenui.demo.domain.usecase

import com.nikhilbhutani.composegenui.demo.data.DemoSettings
import com.nikhilbhutani.composegenui.demo.data.SettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveSettingsUseCase(private val repository: SettingsRepository) {

    operator fun invoke(): Flow<DemoSettings> = repository.settings
}
