package com.justdoit.yoda.entity

data class MessageEntity(
    val toUserId: Int,
    val fromUserId: Int?,
    val fromPhoneNo: String?,
    val sourceType: SourceType
)

data class SourceType(
    val type: Int,
    val isNotified: Boolean,
    val originalBody: String,
    val parsed: String
)

data class MessageResponse(
    val ok: Int,
    val message: List<MessageEntity>?
)

enum class SourceTypeEnum(num: Int) {
    API(0),
    ASTERISK(1)
}

enum class Source {
    MOBILE, LANDLINE, NONUMBER
}
