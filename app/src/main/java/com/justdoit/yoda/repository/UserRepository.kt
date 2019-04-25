package com.justdoit.yoda.repository

import com.justdoit.yoda.APIClient.userService
import com.justdoit.yoda.api.UserService
import com.justdoit.yoda.entity.UserResponse
import retrofit2.Call

class UserRepository: UserService {

    override fun loginByFirebase(token: String): Call<UserResponse?> {
        return userService.loginByFirebase(token)
    }

    override fun login(phoneNumber: String, password: String): Call<UserResponse?> {
        return userService.login(phoneNumber, password)
    }

    override fun createUser(
        phoneNo: String,
        password: String,
        name: String,
        inAppPhoneNo: String
    ): Call<UserResponse?> {
        return userService.createUser(phoneNo, password, name, inAppPhoneNo)
    }

    override fun fetchUser(authToken: String): Call<UserResponse?> {
        return userService.fetchUser(authToken)
    }

    companion object Factory {
        private var instance: UserRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: UserRepository().also { instance = it }
        }
    }
}
