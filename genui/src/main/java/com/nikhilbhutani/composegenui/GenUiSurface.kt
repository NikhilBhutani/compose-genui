package com.nikhilbhutani.composegenui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

/**
 * Composable that binds to a [GenUiConversation] and renders its current document.
 * Automatically wires user actions and events back to the conversation.
 *
 * @param conversation The conversation facade managing the agentic loop
 * @param surfaceId An identifier for this surface (used in user action events)
 * @param loading Composable shown while the generator is producing output
 * @param empty Composable shown when there is no document to render
 */
@Composable
fun GenUiSurface(
    conversation: GenUiConversation,
    surfaceId: String = "default",
    loading: @Composable () -> Unit = { A2UiLoadingIndicator() },
    empty: @Composable () -> Unit = {}
) {
    val document by conversation.document.collectAsState()
    val uiState by conversation.uiState.collectAsState()
    val isLoading by conversation.isLoading.collectAsState()

    when {
        isLoading && document == null -> loading()
        document != null -> {
            A2UiSurface(
                surfaceId = surfaceId,
                document = document!!,
                catalog = conversation.renderCatalog,
                state = uiState,
                onUserAction = { action -> conversation.onUserAction(action) },
                onEvent = { event -> conversation.onEvent(event) }
            )
            if (isLoading) {
                loading()
            }
        }
        else -> empty()
    }
}
