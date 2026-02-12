package com.nikhilbhutani.composegenui.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikhilbhutani.composegenui.demo.data.SettingsRepository
import com.nikhilbhutani.composegenui.demo.domain.usecase.CreateContentGeneratorUseCase
import com.nikhilbhutani.composegenui.demo.domain.usecase.ObserveSettingsUseCase
import com.nikhilbhutani.composegenui.demo.presentation.chat.ChatScreen
import com.nikhilbhutani.composegenui.demo.presentation.chat.ChatViewModel
import com.nikhilbhutani.composegenui.demo.presentation.components.ComponentsScreen
import com.nikhilbhutani.composegenui.demo.presentation.settings.SettingsScreen
import com.nikhilbhutani.composegenui.demo.presentation.settings.SettingsViewModel
import com.nikhilbhutani.composegenui.demo.presentation.templates.TemplatesScreen
import com.nikhilbhutani.composegenui.demo.ui.AccentBlue
import com.nikhilbhutani.composegenui.demo.ui.AppDarkColorScheme
import com.nikhilbhutani.composegenui.demo.ui.DarkSurface
import com.nikhilbhutani.composegenui.demo.ui.TextPrimary
import com.nikhilbhutani.composegenui.demo.ui.TextSecondary

private data class NavTab(val label: String, val icon: ImageVector)

private val tabs = listOf(
    NavTab("Components", Icons.Default.Dashboard),
    NavTab("Templates", Icons.Default.Widgets),
    NavTab("Viewer", Icons.Default.Code),
    NavTab("Settings", Icons.Default.Settings),
)

@Composable
fun DemoApp(settingsRepository: SettingsRepository) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SettingsViewModel(settingsRepository) as T
        }
    )

    val chatViewModel: ChatViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                ChatViewModel(
                    observeSettings = ObserveSettingsUseCase(settingsRepository),
                    createGenerator = CreateContentGeneratorUseCase()
                ) as T
        }
    )

    MaterialTheme(colorScheme = AppDarkColorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var selectedTab by remember { mutableIntStateOf(0) }

            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    NavigationBar(
                        containerColor = DarkSurface,
                        contentColor = TextPrimary,
                        tonalElevation = 0.dp
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            NavigationBarItem(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label, fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = AccentBlue,
                                    selectedTextColor = AccentBlue,
                                    unselectedIconColor = TextSecondary,
                                    unselectedTextColor = TextSecondary,
                                    indicatorColor = AccentBlue.copy(alpha = 0.12f)
                                )
                            )
                        }
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    when (selectedTab) {
                        0 -> ComponentsScreen()
                        1 -> TemplatesScreen()
                        2 -> ChatScreen(chatViewModel)
                        3 -> SettingsScreen(settingsViewModel)
                    }
                }
            }
        }
    }
}
