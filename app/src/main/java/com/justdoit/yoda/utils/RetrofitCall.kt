package com.justdoit.yoda.utils

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Call
import ru.gildor.coroutines.retrofit.awaitResponse

fun <T> Call<T>.exec() = GlobalScope.async {
    try {
        this@exec.awaitResponse().takeIf { it.isSuccessful }?.body()
    } catch (e: Exception) {
        Log.e("RetrofitCall", e.message, e)
        null
    }
}
