package com.justdoit.yoda.entity

data class User(
    val id: Int,
    val name: String,
    val inAppPhoneNo: String,
    val authToken: String
)

data class AuthResponse(
    val ok: Int,
    val user: User
)
