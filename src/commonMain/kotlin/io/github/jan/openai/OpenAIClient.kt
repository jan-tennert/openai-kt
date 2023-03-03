package io.github.jan.openai

import io.github.jan.openai.OpenAiClient.Companion.API_VERSION
import io.github.jan.openai.chat.ChatModule
import io.ktor.client.HttpClient
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.set
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

sealed interface OpenAiClient {

    val chat: ChatModule

    companion object {

        const val API_VERSION = "1"

    }

}

val OpenAIJson = Json {
    encodeDefaults = false
}

internal class OpenAIClientImpl(
    private val apiKey: String
): OpenAiClient {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(OpenAIJson)
        }

        defaultRequest {
            bearerAuth(apiKey)
            url.set("https", "api.openai.com", 443, "/v$API_VERSION/${url.encodedPath}")
        }
        expectSuccess = true
    }

    override val chat: ChatModule = ChatModule(httpClient)

}

fun OpenAiClient(apiKey: String): OpenAiClient = OpenAIClientImpl(apiKey)