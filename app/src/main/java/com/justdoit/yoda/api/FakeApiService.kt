package com.justdoit.yoda.api

import com.justdoit.yoda.entity.FakeApiEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FakeApiService {
    @GET("posts/{userId}")
    fun getPosts(@Path("userId") id: Int): Call<FakeApiEntity>

}
