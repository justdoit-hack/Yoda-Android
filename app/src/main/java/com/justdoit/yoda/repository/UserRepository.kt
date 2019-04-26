package com.justdoit.yoda.repository

import com.justdoit.yoda.APIClient.userService
import com.justdoit.yoda.entity.BaseResponse
import com.justdoit.yoda.entity.UserResponse
import com.justdoit.yoda.utils.asyncWithBaseRes
import kotlinx.coroutines.Deferred
import retrofit2.Call

class UserRepository {
    fun loginByFirebase(token: String): Deferred<BaseResponse<out UserResponse>?> = userService.loginByFirebase(token).asyncWithBaseRes()

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
