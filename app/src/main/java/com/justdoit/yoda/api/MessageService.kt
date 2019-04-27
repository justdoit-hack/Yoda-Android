package com.justdoit.yoda.api

import com.justdoit.yoda.entity.MessageResponse
import com.justdoit.yoda.entity.MessagesResponse
import retrofit2.Call
import retrofit2.http.*

interface MessageService {

    //メッセージ投稿
    @FormUrlEncoded
    @POST("messages/create")
    fun sendMessage(
        @Query("authToken") authToken: String,
        @Field("body") body: String,
        @Field("isAnonymous") isAnonymous: Boolean,
        @Field("toInAppPhoneNo") toInAppPhoneNo: String
    ): Call<MessageResponse>

    //メッセージ送信履歴取得
    @GET("messages/history/send")
    fun getSendMessageHistory(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
        @Query("authToken") authToken: String
    ): Call<MessagesResponse>

    //メッセージ受信履歴取得
    @GET("messages/history/receive")
    fun getReceiveMessageHistory(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?,
        @Query("authToken") authToken: String
    ): Call<MessagesResponse>

    //メッセージ単体取得
    @GET("messages/{messageId}")
    fun getMessage(
        @Path("messageId") messageId: Int,
        @Query("authToken") authToken: String
    ): Call<MessageResponse>
}
