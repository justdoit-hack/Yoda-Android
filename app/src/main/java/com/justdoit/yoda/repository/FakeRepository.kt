package com.justdoit.yoda.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.APIClient.fakeApiService
import com.justdoit.yoda.entity.FakeApiEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FakeRepository {

    fun getPosts(num: Int = 10): LiveData<List<FakeApiEntity?>> {
        val data = MutableLiveData<List<FakeApiEntity?>>()
        val list = arrayListOf<FakeApiEntity?>()

        for (i in 1..num) {
            fakeApiService.getPosts(i).enqueue(object : Callback<FakeApiEntity> {
                override fun onResponse(call: Call<FakeApiEntity>, response: Response<FakeApiEntity>) {
                    list.add(response.body())
                    data.postValue(list)
                    Log.d("API_TEST", "success! ${response.body()}")
                }

                override fun onFailure(call: Call<FakeApiEntity>, t: Throwable) {
                    Log.d("API_TEST", "error! $t")
                }
            })
        }

        return data
    }

    companion object Factory {
        private var instance: FakeRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: FakeRepository().also { instance = it }
        }
    }
}
