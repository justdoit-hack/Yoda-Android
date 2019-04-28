package com.justdoit.yoda.entity

data class MessageEntity(
    val id: Int,
    val toUserId: Int,
    val fromUserId: Int?,
    val fromPhoneNo: String?,
    val sourceType: SourceTypeEnum,
    val isNotified: Boolean,
    val originalBody: String,
    val parsed: String,
    val createdAt: String,
    val updatedAt: String,
    val sendUser: User?
)

data class MessagesResponse(
    val ok: Int,
    val messages: List<MessageEntity>?
)

data class MessageResponse(
    val ok: Int,
    val message: MessageEntity?
)

enum class SourceTypeEnum(val num: Int) {
    API(0),
    ASTERISK(1),
    ANONYMOUS(2)
}
