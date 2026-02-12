@file:OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
    androidx.compose.material3.ExperimentalMaterial3Api::class
)

package com.nikhilbhutani.composegenui.builtins

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nikhilbhutani.composegenui.A2UiEvent
import com.nikhilbhutani.composegenui.A2UiRenderer
import com.nikhilbhutani.composegenui.bool
import com.nikhilbhutani.composegenui.int
import com.nikhilbhutani.composegenui.toModifier
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

internal fun pagerBuiltins(): Map<String, A2UiRenderer> = mapOf(
    "horizontalPager" to @Composable { node, _, _, renderChild ->
        val pageCount = node.children.size
        val initialPage = node.props.int("initialPage") ?: 0
        val pagerState = rememberPagerState(initialPage = initialPage) { pageCount }
        HorizontalPager(
            modifier = node.props.toModifier(),
            state = pagerState
        ) { page ->
            if (page < node.children.size) {
                renderChild(node.children[page])
            }
        }
    },
    "verticalPager" to @Composable { node, _, _, renderChild ->
        val pageCount = node.children.size
        val initialPage = node.props.int("initialPage") ?: 0
        val pagerState = rememberPagerState(initialPage = initialPage) { pageCount }
        VerticalPager(
            modifier = node.props.toModifier(),
            state = pagerState
        ) { page ->
            if (page < node.children.size) {
                renderChild(node.children[page])
            }
        }
    },
    "page" to { node, _, _, renderChild ->
        Box(modifier = node.props.toModifier()) {
            for (child in node.children) {
                renderChild(child)
            }
        }
    },
    "datePicker" to @Composable { node, _, onEvent, _ ->
        val id = node.id ?: "datePicker"
        val visible = node.props.bool("visible") ?: true
        val datePickerState = rememberDatePickerState()
        if (visible) {
            DatePickerDialog(
                onDismissRequest = { onEvent(A2UiEvent(id, "dismiss")) },
                confirmButton = {
                    TextButton(onClick = {
                        val selectedMs = datePickerState.selectedDateMillis
                        onEvent(A2UiEvent(id, "confirm", JsonObject(mapOf("dateMs" to JsonPrimitive(selectedMs ?: 0L)))))
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { onEvent(A2UiEvent(id, "dismiss")) }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    },
    "timePicker" to @Composable { node, _, onEvent, _ ->
        val id = node.id ?: "timePicker"
        val initialHour = node.props.int("hour") ?: 12
        val initialMinute = node.props.int("minute") ?: 0
        val is24Hour = node.props.bool("is24Hour") ?: false
        val timePickerState = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute, is24Hour = is24Hour)
        Column(modifier = node.props.toModifier()) {
            TimePicker(state = timePickerState)
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { onEvent(A2UiEvent(id, "dismiss")) }) { Text("Cancel") }
                TextButton(onClick = {
                    onEvent(A2UiEvent(id, "confirm", JsonObject(mapOf(
                        "hour" to JsonPrimitive(timePickerState.hour),
                        "minute" to JsonPrimitive(timePickerState.minute)
                    ))))
                }) { Text("OK") }
            }
        }
    }
)
