package com.justdoit.yoda.entity

data class MessageResponse(
    val phoneNumber: String,
    val userId: String,
    val messageNum: String,
    val originalDigits: String,
    val secret: String
)
