package com.justdoit.yoda

import com.justdoit.yoda.api.UserService
import com.justdoit.yoda.api.FakeApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object APIClient {
    private const val API_URL = "https://yoda.wintu.dev/api/"
    private const val TEST_API_URL = "https://jsonplaceholder.typicode.com"

    val fakeApiService: FakeApiService = create(FakeApiService::class.java)
    val userService: UserService = create(UserService::class.java)
    //TODO 各Serviceの追加

    private lateinit var retrofit: Retrofit

    fun <S> create(serviceClass: Class<S>): S {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        // create retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)  //FIXME テスト用API以外のもの実装したら置き換え
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(serviceClass)
    }
}
