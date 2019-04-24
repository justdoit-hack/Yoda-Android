package com.justdoit.yoda.api

import com.justdoit.yoda.entity.AuthResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("phoneNo") phoneNumber: String,
        @Field("password") password: String
    ): Call<AuthResponse>
}
