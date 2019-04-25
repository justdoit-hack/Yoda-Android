package com.justdoit.yoda.api

import com.justdoit.yoda.entity.MessageResponse
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
    )

    //メッセージ送信履歴取得
    @GET("messages/history/send")
    fun getSendMessageHistory(
        limit: Int,
        offset: Int,
        @Query("authToken") authToken: String
    ): Call<List<MessageResponse>>

    //メッセージ受信履歴取得
    @GET("messages/history/receive")
    fun getReceiveMessageHistory(
        limit: Int,
        offset: Int,
        @Query("authToken") authToken: String
    ): Call<List<MessageResponse>>

    //メッセージ単体取得
    @GET("messages")
    fun getMessage(
        @Query("messageId") messageId: String,
        @Query("authToken") authToken: String
    ): Call<MessageResponse>
}
