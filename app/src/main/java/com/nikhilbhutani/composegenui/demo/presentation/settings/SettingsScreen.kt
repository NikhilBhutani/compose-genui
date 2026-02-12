package com.nikhilbhutani.composegenui.demo.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikhilbhutani.composegenui.demo.data.LlmProviderOption
import com.nikhilbhutani.composegenui.demo.ui.AccentBlue
import com.nikhilbhutani.composegenui.demo.ui.AccentTeal
import com.nikhilbhutani.composegenui.demo.ui.DarkSurface
import com.nikhilbhutani.composegenui.demo.ui.DarkSurfaceVariant
import com.nikhilbhutani.composegenui.demo.ui.InfoRow
import com.nikhilbhutani.composegenui.demo.ui.TextPrimary
import com.nikhilbhutani.composegenui.demo.ui.TextSecondary

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(Modifier.height(24.dp))

        // LLM Provider
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("LLM Provider", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                Spacer(Modifier.height(12.dp))

                LlmProviderOption.entries.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = settings.provider == option,
                            onClick = { viewModel.updateProvider(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = AccentBlue)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(option.label, fontSize = 14.sp, color = TextPrimary)
                            if (option != LlmProviderOption.DEMO) {
                                Text(
                                    "Default: ${option.defaultModel}",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // API Key + Model (only when not DEMO)
        if (settings.provider != LlmProviderOption.DEMO) {
            Spacer(Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("API Configuration", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = settings.apiKey,
                        onValueChange = { viewModel.updateApiKey(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("API Key") },
                        placeholder = { Text("Enter your API key...", color = TextSecondary) },
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant,
                            focusedBorderColor = AccentBlue,
                            unfocusedBorderColor = Color(0xFF30363D),
                            cursorColor = AccentBlue,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = AccentBlue,
                            unfocusedLabelColor = TextSecondary,
                        ),
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = settings.model,
                        onValueChange = { viewModel.updateModel(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Model") },
                        placeholder = { Text(settings.provider.defaultModel, color = TextSecondary) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant,
                            focusedBorderColor = AccentBlue,
                            unfocusedBorderColor = Color(0xFF30363D),
                            cursorColor = AccentBlue,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedLabelColor = AccentBlue,
                            unfocusedLabelColor = TextSecondary,
                        ),
                        singleLine = true
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // About
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Compose GenUI", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Text("A2UI Renderer for Jetpack Compose", fontSize = 13.sp, color = TextSecondary)
                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFF21262D))
                Spacer(Modifier.height(16.dp))

                InfoRow("Version", "1.0.0")
                InfoRow("Components", "55+")
                InfoRow("LLM Providers", "OpenAI, Anthropic, Gemini")
                InfoRow("Min SDK", "24")
                InfoRow("Compose BOM", "2025.01.01")
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Features", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                Spacer(Modifier.height(12.dp))

                val features = listOf(
                    "JSON-driven UI rendering",
                    "Material 3 component catalog",
                    "Schema validation",
                    "Chat-based UI generation",
                    "Multi-LLM support",
                    "User action callbacks"
                )
                features.forEach { feature ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AccentTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(feature, fontSize = 14.sp, color = TextPrimary)
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
