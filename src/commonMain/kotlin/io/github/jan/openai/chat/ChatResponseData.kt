package io.github.jan.openai.chat

import io.github.jan.openai.UsageData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatResponseData(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<ChatResponseChoice>,
    val usage: UsageData
)

@Serializable
data class ChatResponseChoice(
    val index: Int,
    val message: ChatMessageData,
    @SerialName("finish_reason")
    val finishReason: String?
)