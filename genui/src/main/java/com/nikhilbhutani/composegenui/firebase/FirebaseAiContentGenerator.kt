package com.nikhilbhutani.composegenui.firebase

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import com.nikhilbhutani.composegenui.GenUiContentGenerator
import com.nikhilbhutani.composegenui.GenUiMessage
import com.nikhilbhutani.composegenui.GenUiMessageRole
import com.nikhilbhutani.composegenui.GenUiRequest
import com.nikhilbhutani.composegenui.GenUiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * [GenUiContentGenerator] backed by Firebase AI Logic SDK.
 *
 * Uses Firebase's managed connection to Gemini — no raw API key in the app,
 * rate limiting and abuse protection built in.
 *
 * Requires Firebase to be initialized in your Application class and the
 * Firebase AI dependency on your classpath. Add to your app's `build.gradle.kts`:
 *
 * ```kotlin
 * implementation(platform("com.google.firebase:firebase-bom:34.8.0"))
 * implementation("com.google.firebase:firebase-ai")
 * ```
 *
 * @param modelName Gemini model to use (default: gemini-2.0-flash)
 * @param backend [GenerativeBackend.googleAI] for free tier / prototyping,
 *                [GenerativeBackend.vertexAI] for production with billing
 * @throws IllegalStateException if Firebase AI SDK is not on the classpath
 */
class FirebaseAiContentGenerator(
    private val modelName: String = "gemini-2.0-flash",
    private val backend: GenerativeBackend = GenerativeBackend.googleAI()
) : GenUiContentGenerator {

    init {
        try {
            Class.forName("com.google.firebase.ai.FirebaseAI")
        } catch (_: ClassNotFoundException) {
            throw IllegalStateException(
                "Firebase AI SDK not found on classpath. " +
                "Add the following to your app's build.gradle.kts:\n" +
                "  implementation(platform(\"com.google.firebase:firebase-bom:34.8.0\"))\n" +
                "  implementation(\"com.google.firebase:firebase-ai\")"
            )
        }
    }

    private var cachedModel: GenerativeModel? = null
    private var cachedSystemPrompt: String? = null

    private fun getModel(systemPrompt: String): GenerativeModel {
        if (cachedModel != null && cachedSystemPrompt == systemPrompt) {
            return cachedModel!!
        }
        val model = Firebase.ai(backend = backend).generativeModel(
            modelName = modelName,
            systemInstruction = content { text(systemPrompt) },
            generationConfig = generationConfig {
                responseMimeType = "application/json"
            }
        )
        cachedModel = model
        cachedSystemPrompt = systemPrompt
        return model
    }

    override fun generate(request: GenUiRequest): Flow<GenUiResponse> = flow {
        try {
            val model = getModel(request.systemPrompt)

            val history = request.history.map { msg ->
                content(role = msg.toGeminiRole()) { text(msg.content) }
            }

            val chat = model.startChat(history = history)
            val response = chat.sendMessage(request.userMessage)
            val responseText = response.text
                ?: throw Exception("Empty response from Firebase AI")

            val jsonStr = extractJson(responseText)
            emit(GenUiResponse.UiDocument(jsonStr))
            emit(GenUiResponse.Done)
        } catch (e: Exception) {
            emit(GenUiResponse.Error(e.message ?: "Firebase AI call failed", e))
            emit(GenUiResponse.Done)
        }
    }.flowOn(Dispatchers.IO)

    override fun clearHistory() {
        cachedModel = null
        cachedSystemPrompt = null
    }

    private fun GenUiMessage.toGeminiRole(): String = when (role) {
        GenUiMessageRole.USER, GenUiMessageRole.SYSTEM -> "user"
        GenUiMessageRole.ASSISTANT -> "model"
    }

    private fun extractJson(response: String): String {
        val trimmed = response.trim()
        val jsonBlockRegex = Regex("```(?:json)?\\s*\\n?([\\s\\S]*?)\\n?```")
        val match = jsonBlockRegex.find(trimmed)
        if (match != null) return match.groupValues[1].trim()

        val startIndex = trimmed.indexOf('{')
        val endIndex = trimmed.lastIndexOf('}')
        if (startIndex != -1 && endIndex > startIndex) {
            return trimmed.substring(startIndex, endIndex + 1)
        }
        return trimmed
    }
}
