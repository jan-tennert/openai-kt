package io.github.jan.openai.chat

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatSession(private val requestData: ChatRequestData, private val chatModule: ChatModule) {

    private val messageFlow = MutableStateFlow<List<ChatMessageData>>(requestData.messages)
    val messages = messageFlow.asStateFlow()
    private val statusFlow = MutableStateFlow<ChatStatus>(ChatStatus.IDLE)
    val status = statusFlow.asStateFlow()

    suspend fun request(message: String): String {
        statusFlow.value = ChatStatus.ASKING
        val newMessage = ChatMessageData.user(message)
        messageFlow.value = messages.value + newMessage
        val requestData = buildRequestData(newMessage)
        val response = chatModule.request(requestData)
        val responseMessage = response.choices.first().message
        messageFlow.value = messages.value + responseMessage
        statusFlow.value = ChatStatus.IDLE
        return responseMessage.content
    }

    private fun buildRequestData(newMessage: ChatMessageData) = requestData.copy(messages = messages.value + newMessage)

}

enum class ChatStatus {

    IDLE, ASKING

}