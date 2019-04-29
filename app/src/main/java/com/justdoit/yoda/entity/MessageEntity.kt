package com.justdoit.yoda.entity

import java.io.Serializable


data class MessageEntity(
    val id: Int,
    val toUserId: Int,
    val fromUserId: Int?,
    val fromPhoneNo: String?,
    val sourceType: Int,
    val isNotified: Boolean,
    val originalBody: String,
    val parsed: String,
    val createdAt: String,
    val updatedAt: String,
    val fromUser: User?
): Serializable

data class MessagesResponse(
    val ok: Int,
    val messages: List<MessageEntity>?
)

data class MessageResponse(
    val ok: Int,
    val message: MessageEntity?
)

enum class SourceTypeEnum(num: Int) {
    API(0),
    ASTERISK(1)
}
