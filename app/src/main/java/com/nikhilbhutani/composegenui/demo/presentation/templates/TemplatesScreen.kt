package com.nikhilbhutani.composegenui.demo.presentation.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikhilbhutani.composegenui.A2UiJson
import com.nikhilbhutani.composegenui.A2UiRender
import com.nikhilbhutani.composegenui.A2UiState
import com.nikhilbhutani.composegenui.defaultGenUiCatalog
import com.nikhilbhutani.composegenui.demo.ui.AccentBlue
import com.nikhilbhutani.composegenui.demo.ui.DarkSurface
import com.nikhilbhutani.composegenui.demo.ui.TextPrimary
import com.nikhilbhutani.composegenui.demo.ui.TextSecondary
import kotlinx.serialization.json.JsonElement

@Composable
fun TemplatesScreen() {
    val templates = listOf(
        "Login" to LOGIN_TEMPLATE_JSON,
        "Profile" to PROFILE_TEMPLATE_JSON,
        "Layout" to LAYOUT_DEMO_JSON,
        "Input Controls" to INPUT_DEMO_JSON,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            "A2UI Templates",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Pre-built templates rendered via A2UiRender",
            fontSize = 13.sp,
            color = TextSecondary
        )
        Spacer(Modifier.height(24.dp))

        templates.forEach { (name, json) ->
            TemplateCard(name, json)
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun TemplateCard(name: String, json: String) {
    val catalog = remember { defaultGenUiCatalog() }
    val document = remember(json) { A2UiJson.decodeDocument(json) }
    val state = remember { mutableStateMapOf<String, JsonElement>() }
    val uiState by remember { derivedStateOf { A2UiState(state.toMap()) } }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextPrimary)
                AssistChip(
                    onClick = {},
                    label = { Text("A2UI", fontSize = 10.sp) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = AccentBlue.copy(alpha = 0.15f),
                        labelColor = AccentBlue
                    ),
                    border = null
                )
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF21262D))
            Spacer(Modifier.height(12.dp))
            A2UiRender(
                document = document,
                catalog = catalog.asA2UiCatalog(),
                state = uiState,
                onEvent = { event ->
                    if (event.action == "input") {
                        val value = event.payload["value"]
                        if (value != null) state[event.nodeId] = value
                    }
                }
            )
        }
    }
}
