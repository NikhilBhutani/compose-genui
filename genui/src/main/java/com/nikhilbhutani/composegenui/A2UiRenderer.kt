package com.nikhilbhutani.composegenui

import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** Renders an [A2UiDocument] using the given [catalog], [state], and event handler. */
@Composable
fun A2UiRender(
    document: A2UiDocument,
    catalog: A2UiCatalog,
    state: A2UiState,
    onEvent: (A2UiEvent) -> Unit
) {
    RenderNode(document.root, catalog, state, onEvent)
}

/** Lower-level surface that wraps [A2UiRender] and converts events into [A2UiUserAction]s. */
@Composable
fun A2UiSurface(
    surfaceId: String,
    document: A2UiDocument,
    catalog: A2UiCatalog,
    state: A2UiState,
    onUserAction: (A2UiUserAction) -> Unit,
    onEvent: ((A2UiEvent) -> Unit)? = null
) {
    A2UiRender(
        document = document,
        catalog = catalog,
        state = state,
        onEvent = { event ->
            val timestamp = a2uiTimestamp()
            val surfacedEvent = event.copy(surfaceId = surfaceId, timestamp = timestamp)
            onEvent?.invoke(surfacedEvent)
            onUserAction(
                A2UiUserAction(
                    surfaceId = surfaceId,
                    sourceComponentId = event.nodeId,
                    actionName = event.action,
                    context = event.payload,
                    timestamp = timestamp
                )
            )
        }
    )
}

@Composable
fun RenderNode(
    node: A2UiNode,
    catalog: A2UiCatalog,
    state: A2UiState,
    onEvent: (A2UiEvent) -> Unit
) {
    catalog.Render(node, state, onEvent) { child ->
        RenderNode(child, catalog, state, onEvent)
    }
}

private fun a2uiTimestamp(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date())
}
