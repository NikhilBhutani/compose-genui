package com.nikhilbhutani.composegenui.demo.presentation.chat

import com.nikhilbhutani.composegenui.GenUiContentGenerator
import com.nikhilbhutani.composegenui.GenUiRequest
import com.nikhilbhutani.composegenui.GenUiResponse
import com.nikhilbhutani.composegenui.demo.presentation.templates.FALLBACK_TEMPLATE_JSON
import com.nikhilbhutani.composegenui.demo.presentation.templates.LOGIN_TEMPLATE_JSON
import com.nikhilbhutani.composegenui.demo.presentation.templates.PROFILE_TEMPLATE_JSON
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DemoContentGenerator : GenUiContentGenerator {

    override fun generate(request: GenUiRequest): Flow<GenUiResponse> = flow {
        val prompt = request.userMessage
        val json = when {
            prompt.contains("login", ignoreCase = true) -> LOGIN_TEMPLATE_JSON
            prompt.contains("profile", ignoreCase = true) -> PROFILE_TEMPLATE_JSON
            else -> FALLBACK_TEMPLATE_JSON
        }
        emit(GenUiResponse.UiDocument(json))
        emit(GenUiResponse.Done)
    }

    override fun clearHistory() {}
}
