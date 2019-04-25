package com.justdoit.yoda.api

import com.justdoit.yoda.entity.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    //Firebase Authで取ってきたTokenを使ったログイン
    @FormUrlEncoded
    @POST
    fun loginByFirebase(
        @Field("token") token: String
    ): Call<UserResponse?>

    //電話番号とパスワードを使ったログイン
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("phoneNo") phoneNumber: String,
        @Field("password") password: String
    ): Call<UserResponse?>

    //ユーザー登録
    @FormUrlEncoded
    @POST("users/create")
    fun createUser(
        @Field("phoneNo") phoneNo: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("inAppPhoneNo") inAppPhoneNo: String
    ): Call<UserResponse?>

    //ユーザー情報のフェッチ
    @GET("users/fetch")
    fun fetchUser(
        @Query("authToken") authToken: String
    ): Call<UserResponse?>
}
