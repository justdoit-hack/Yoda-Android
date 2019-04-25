package com.justdoit.yoda.entity

data class MessageEntity(
    val id: String,
    val source: Source,
    val userId: String,
    val message: String
)

enum class Source {
    MOBILE, LANDLINE, NONUMBER
}