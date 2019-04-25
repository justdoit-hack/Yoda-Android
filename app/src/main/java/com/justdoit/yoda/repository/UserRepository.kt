package com.justdoit.yoda.repository

import com.justdoit.yoda.APIClient.userService
import com.justdoit.yoda.entity.UserResponse
import retrofit2.Call

class UserRepository {

/* @Example
val userRepository = UserRepository.getInstance()
GlobalScope.launch(Dispatchers.Main) {
    withContext(Dispatchers.Default) {
        try {
            userRepository.login(phoneNumber, password).awaitResponse().body()?.let {
                Log.d("Login", "Login Success!")
                Log.d("Login", it.user.authToken)
                //TODO SharedPreferenceで保存して
            }
        } catch (e: Throwable) {
            Log.e("Error", e.message)
        }
    }
}
*/

    fun loginByFirebase(token: String): Call<UserResponse?> {
        return userService.loginByFirebase(token)
    }

    fun login(phoneNumber: String, password: String): Call<UserResponse?> {
        return userService.login(phoneNumber, password)
    }

    companion object Factory {
        private var instance: UserRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: UserRepository().also { instance = it }
        }
    }
}
