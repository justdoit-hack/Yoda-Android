package com.justdoit.yoda.utils

import android.util.Log
import com.justdoit.yoda.entity.BaseResponse
import com.justdoit.yoda.entity.ErrorResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import ru.gildor.coroutines.retrofit.awaitResponse

fun <T> Call<T>.asyncWithBaseRes() = GlobalScope.async {
    try {
        val result = this@asyncWithBaseRes.awaitResponse()
        if (result.isSuccessful) BaseResponse(result.body()) else {
            val errorJson = result.errorBody()?.string() ?: return@async null
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            BaseResponse(null, true, moshi.adapter(ErrorResponse::class.java).fromJson(errorJson))
        }
    } catch (e: Exception) {
        Log.e("RetrofitCall", e.message, e)
        null
    }
}
