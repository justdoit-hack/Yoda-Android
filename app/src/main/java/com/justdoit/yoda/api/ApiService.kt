package com.justdoit.yoda.api

import com.justdoit.yoda.entity.MessageEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    companion object {
        const val API_URL = ""
    }

    @POST("")
    fun sendMessage()

    @GET("")
    fun getMessage(): Call<MessageEntity>

}
