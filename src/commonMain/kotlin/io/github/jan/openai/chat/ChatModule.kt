package io.github.jan.openai.chat

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ChatModule(@PublishedApi internal val httpClient: HttpClient) {

    suspend inline fun request(data: ChatRequestData): ChatResponseData {
        return httpClient.post("chat/completions") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }.body()
    }

}