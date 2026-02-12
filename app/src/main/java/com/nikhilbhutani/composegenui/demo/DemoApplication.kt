package com.nikhilbhutani.composegenui.demo

import android.app.Application
import com.nikhilbhutani.composegenui.demo.data.SettingsRepository

class DemoApplication : Application() {

    lateinit var settingsRepository: SettingsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        settingsRepository = SettingsRepository(this)
    }
}
