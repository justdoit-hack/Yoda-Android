package com.justdoit.yoda.repository

import com.justdoit.yoda.APIClient.userService
import com.justdoit.yoda.entity.UserResponse
import retrofit2.Call

class UserRepository {
    fun loginByFirebase(token: String): Call<UserResponse> {
        return userService.loginByFirebase(token)
    }

    fun login(phoneNumber: String, password: String): Call<UserResponse> {
        return userService.login(phoneNumber, password)
    }

    companion object Factory {
        private var instance: UserRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: UserRepository().also { instance = it }
        }
    }
}
