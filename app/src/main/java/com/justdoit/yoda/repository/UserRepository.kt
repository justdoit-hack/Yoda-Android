package com.justdoit.yoda.repository

import com.justdoit.yoda.APIClient.userService
import com.justdoit.yoda.entity.BaseResponse
import com.justdoit.yoda.entity.UserResponse
import com.justdoit.yoda.utils.asyncWithBaseRes
import kotlinx.coroutines.Deferred

class UserRepository {

    fun loginByFirebase(token: String): Deferred<BaseResponse<out UserResponse>?> {
        return userService.loginByFirebase(token).asyncWithBaseRes()
    }

    fun login(phoneNumber: String, password: String): Deferred<BaseResponse<out UserResponse>?> {
        return userService.login(phoneNumber, password).asyncWithBaseRes()
    }

    fun fetchUser(authToken: String): Deferred<BaseResponse<out UserResponse>?> {
        return userService.fetchUser(authToken).asyncWithBaseRes()
    }

    companion object Factory {
        private var instance: UserRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: UserRepository().also { instance = it }
        }
    }
}
