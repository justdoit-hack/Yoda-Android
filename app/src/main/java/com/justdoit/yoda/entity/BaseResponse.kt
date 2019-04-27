package com.justdoit.yoda.entity

data class ErrorResponse (
    val code: String,
    val name: String,
    val message: String
)

data class BaseResponse<T> (
    val body: T?,
    val hasError: Boolean = false,
    val error: ErrorResponse? = null
)

data class OkayResponse (
    val ok: Int
)


