package com.justdoit.yoda.api

import com.justdoit.yoda.entity.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserService {

    @FormUrlEncoded
    @POST("auth/firebase/verify")
    fun loginByFirebase(
        @Field("token") token: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("phoneNo") phoneNumber: String,
        @Field("password") password: String
    ): Call<UserResponse>
}
