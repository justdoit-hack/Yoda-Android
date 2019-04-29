package com.justdoit.yoda

import com.justdoit.yoda.adapter.CustomJsonAdapter
import com.justdoit.yoda.api.MessageService
import com.justdoit.yoda.api.UserService
import com.justdoit.yoda.entity.SourceTypeEnum
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

object APIClient {
    private const val API_URL = "https://yoda.wintu.dev/api/"

    val userService: UserService = create(UserService::class.java)
    val messageService: MessageService = create(MessageService::class.java)
    //TODO 各Serviceの追加

    private lateinit var retrofit: Retrofit

    fun <S> create(serviceClass: Class<S>): S {
        val moshi = Moshi.Builder()
            .add(CustomJsonAdapter.Factory)
            .add(KotlinJsonAdapterFactory())
            .build()

        // create retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)  //FIXME テスト用API以外のもの実装したら置き換え
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(serviceClass)
    }
}
