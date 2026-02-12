package com.nikhilbhutani.composegenui.demo.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nikhilbhutani.composegenui.demo.ui.AccentBlue
import com.nikhilbhutani.composegenui.demo.ui.AccentSection
import com.nikhilbhutani.composegenui.demo.ui.AccentTeal
import com.nikhilbhutani.composegenui.demo.ui.ComponentShowcaseCard
import com.nikhilbhutani.composegenui.demo.ui.DarkSurfaceVariant
import com.nikhilbhutani.composegenui.demo.ui.SelectionRow
import com.nikhilbhutani.composegenui.demo.ui.TextPrimary
import com.nikhilbhutani.composegenui.demo.ui.TextSecondary

@Composable
fun ComponentsScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "M3 Library",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "COMPOSE GENUI V1.0",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentBlue,
                    letterSpacing = 2.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AccentBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search components...", color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceVariant,
                unfocusedContainerColor = DarkSurfaceVariant,
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = AccentBlue,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
            ),
            singleLine = true
        )

        Spacer(Modifier.height(28.dp))

        // Buttons section
        AccentSection("Buttons", accentColor = AccentBlue) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ComponentShowcaseCard(
                    label = "Filled",
                    subtitle = "Elevated Primary",
                    modifier = Modifier.weight(1f)
                ) {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Filled", fontSize = 13.sp)
                    }
                }
                ComponentShowcaseCard(
                    label = "Tonal",
                    subtitle = "Secondary Action",
                    modifier = Modifier.weight(1f)
                ) {
                    FilledTonalButton(
                        onClick = {},
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = AccentBlue.copy(alpha = 0.15f),
                            contentColor = AccentBlue
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Tonal", fontSize = 13.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ComponentShowcaseCard(
                    label = "Outlined",
                    subtitle = "Low Emphasis",
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedButton(
                        onClick = {},
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentTeal),
                        border = ButtonDefaults.outlinedButtonBorder(true).copy(
                            brush = SolidColor(AccentTeal)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Outlined", fontSize = 13.sp)
                    }
                }
                ComponentShowcaseCard(
                    label = "Elevated",
                    subtitle = "Surface Tint",
                    modifier = Modifier.weight(1f)
                ) {
                    ElevatedButton(
                        onClick = {},
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = DarkSurfaceVariant,
                            contentColor = AccentTeal
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Elevated", fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Selection section
        AccentSection("Selection", accentColor = AccentTeal) {
            SelectionRow(
                icon = Icons.Default.CheckBox,
                label = "Checkbox",
                tag = "Boolean",
                tagColor = AccentBlue
            ) {
                Checkbox(
                    checked = true,
                    onCheckedChange = {},
                    colors = CheckboxDefaults.colors(
                        checkedColor = AccentBlue,
                        checkmarkColor = Color.White
                    )
                )
            }
            Spacer(Modifier.height(8.dp))
            SelectionRow(
                icon = Icons.Default.RadioButtonChecked,
                label = "Radio Button",
                tag = "Single",
                tagColor = AccentTeal
            ) {
                RadioButton(
                    selected = true,
                    onClick = {},
                    colors = RadioButtonDefaults.colors(selectedColor = AccentTeal)
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.ToggleOn, contentDescription = null, tint = TextSecondary)
                    Text("Switch", color = TextPrimary, fontSize = 14.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = AccentBlue
                        )
                    )
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = Color(0xFF30363D)
                        )
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Input Fields section
        AccentSection("Input Fields", accentColor = Color(0xFFCE93D8)) {
            OutlinedTextField(
                value = "Active Input State",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                label = { Text("LABEL TEXT", fontSize = 11.sp, letterSpacing = 1.sp) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = AccentBlue,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = AccentBlue,
                    unfocusedLabelColor = AccentBlue,
                    cursorColor = AccentBlue,
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter value...", color = TextSecondary) },
                label = { Text("PLACEHOLDER", fontSize = 11.sp, letterSpacing = 1.sp) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurfaceVariant,
                    unfocusedContainerColor = DarkSurfaceVariant,
                    focusedBorderColor = Color(0xFF30363D),
                    unfocusedBorderColor = Color(0xFF30363D),
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedLabelColor = TextSecondary,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = AccentBlue,
                )
            )
        }

        Spacer(Modifier.height(24.dp))

        // Display section
        AccentSection("Display", accentColor = Color(0xFFFFB74D)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2744))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Featured Component",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "A sample card with rich content, showcasing Material 3 design patterns",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(
                            onClick = {},
                            label = { Text("Material 3", fontSize = 11.sp) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = AccentBlue.copy(alpha = 0.15f),
                                labelColor = AccentBlue
                            ),
                            border = null
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text("Compose", fontSize = 11.sp) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = AccentTeal.copy(alpha = 0.15f),
                                labelColor = AccentTeal
                            ),
                            border = null
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            // Dashed add-component placeholder
            val outlineColor = TextSecondary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = outlineColor,
                            style = Stroke(
                                width = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            ),
                            cornerRadius = CornerRadius(16.dp.toPx())
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Add Component", color = TextSecondary, fontSize = 13.sp)
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}
