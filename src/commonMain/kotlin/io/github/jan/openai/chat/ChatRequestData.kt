package io.github.jan.openai.chat

import io.github.jan.openai.OpenAiModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ChatRequestData(
    @Required
    var model: String = OpenAiModel.Chat.GPT_3_5_TURBO,
    var temperature: Float = 1.0f,
    @SerialName("top_p")
    var topP: Float = 1.0f,
    var n: Int = 1,
    internal var stream: Boolean = false,
    @SerialName("max_tokens")
    var maxTokens: Int? = null,
    @SerialName("presence_penalty")
    var presencePenalty: Float = 0.0f,
    @SerialName("frequency_penalty")
    var frequencyPenalty: Float = 0.0f,
    var user: String? = null,
    @Required
    val messages: List<ChatMessageData> = listOf()
)

@Serializable
data class ChatMessageData(
    val role: Role,
    val content: String
) {

    @Serializable(with = Role.Companion::class)
    enum class Role {
        USER, ASSISTANT, SYSTEM;

        companion object : KSerializer<Role> {

            override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Role", PrimitiveKind.STRING)
            override fun deserialize(decoder: Decoder): Role = valueOf(decoder.decodeString().uppercase())
            override fun serialize(encoder: Encoder, value: Role) {
                encoder.encodeString(value.name.lowercase())
            }

        }

    }

    companion object {
        fun user(content: String) = ChatMessageData(Role.USER, content)
        fun assistant(content: String) = ChatMessageData(Role.ASSISTANT, content)
        fun system(content: String) = ChatMessageData(Role.SYSTEM, content)
    }

}
