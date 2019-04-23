package com.justdoit.yoda.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.api.FakeApiService
import com.justdoit.yoda.entity.FakeApiEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FakeRepository {
    private var fakeApiService: FakeApiService

    init {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(FakeApiService.API_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        fakeApiService = retrofit.create(FakeApiService::class.java)
    }

    fun getPosts(id: Int = 1): LiveData<FakeApiEntity> {
        val data = MutableLiveData<FakeApiEntity>()

        fakeApiService.getPosts(id).enqueue(object : Callback<FakeApiEntity> {
            override fun onResponse(call: Call<FakeApiEntity>, response: Response<FakeApiEntity>) {
                data.postValue(response.body())
                Log.d("API_TEST", "success! ${response.body()}")
            }

            override fun onFailure(call: Call<FakeApiEntity>, t: Throwable) {
                Log.d("API_TEST", "error! $t")
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
