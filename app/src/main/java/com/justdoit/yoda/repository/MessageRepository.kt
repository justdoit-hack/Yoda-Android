package com.justdoit.yoda.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.api.ApiService
import com.justdoit.yoda.api.ApiService.Companion.API_URL
import com.justdoit.yoda.entity.MessageEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MessageRepository {

    private var apiService: ApiService

    init {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    fun sendMessage(phoneMessage: String) {
        apiService.sendMessage()
    }

    fun getMessage(): LiveData<MessageEntity> {
        val data = MutableLiveData<MessageEntity>()

        apiService.getMessage().enqueue(object : Callback<MessageEntity> {
            override fun onResponse(call: Call<MessageEntity>, response: Response<MessageEntity>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onFailure(call: Call<MessageEntity>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        return data
    }

    companion object Factory {
        private var instance: MessageRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: MessageRepository().also { instance = it }
        }
    }
}
