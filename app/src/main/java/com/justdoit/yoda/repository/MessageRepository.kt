package com.justdoit.yoda.repository

import com.justdoit.yoda.APIClient.messageService
import com.justdoit.yoda.entity.BaseResponse
import com.justdoit.yoda.entity.MessageResponse
import com.justdoit.yoda.utils.asyncWithBaseRes
import kotlinx.coroutines.Deferred

class MessageRepository {
    fun sendMessage(
        authToken: String,
        body: String,
        isAnonymous: Boolean,
        toInAppPhoneNo: String
    ): Deferred<BaseResponse<out MessageResponse>?> =
        messageService.sendMessage(authToken, body, isAnonymous, toInAppPhoneNo).asyncWithBaseRes()


    fun getSendMessageHistory(
        limit: Int,
        offset: Int,
        authToken: String
    ): Deferred<BaseResponse<out MessageResponse>?> =
        messageService.getSendMessageHistory(limit, offset, authToken).asyncWithBaseRes()

    fun getReceiveMessageHistory(
        limit: Int,
        offset: Int,
        authToken: String
    ): Deferred<BaseResponse<out MessageResponse>?> =
        messageService.getReceiveMessageHistory(limit, offset, authToken).asyncWithBaseRes()

    fun getMessage(messageId: String, authToken: String): Deferred<BaseResponse<out MessageResponse>?> =
        messageService.getMessage(messageId, authToken).asyncWithBaseRes()

    companion object Factory {
        private var instance: MessageRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: MessageRepository().also { instance = it }
        }
    }
}
