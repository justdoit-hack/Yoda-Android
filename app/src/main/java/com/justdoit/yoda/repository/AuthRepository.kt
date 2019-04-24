package com.justdoit.yoda.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.APIClient.authService
import com.justdoit.yoda.entity.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// @Example
/*

val authRepository = AuthRepository.getInstance()
val phoneNumber = "08067788700"
val password = "test1234"
val authToken = authRepository.login(phoneNumber, password)
authToken.observe(this, Observer {
    if (it != null) {
        Log.d("Login", "Login Success!")
    } else {
        Log.d("Login", "Login Failed")
    }
})

*/

class AuthRepository {

    fun login(phoneNumber: String, password: String): LiveData<String?> {
        val authToken = MutableLiveData<String>()

        authService.login(phoneNumber, password).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        authToken.postValue(it.user.authToken)
                    }
                } else {
                    Log.d("Login", "${response.errorBody()}")
                    authToken.postValue(null)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("Login", t.message)
                authToken.postValue(null)
            }

        })
        return authToken
    }

    companion object Factory {
        private var instance: AuthRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AuthRepository().also { instance = it }
        }
    }
}
