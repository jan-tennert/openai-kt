package io.github.jan.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageData(
    @SerialName("prompt_tokens")
    val promptTokens: Int,
    @SerialName("completion_tokens")
    val completionTokens: Int,
    @SerialName("total_tokens")
    val totalTokens: Int,
)