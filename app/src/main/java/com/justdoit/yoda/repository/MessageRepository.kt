package com.justdoit.yoda.repository

import com.justdoit.yoda.APIClient.messageService
import com.justdoit.yoda.api.MessageService
import com.justdoit.yoda.entity.MessageResponse
import retrofit2.Call

class MessageRepository : MessageService {
    override fun sendMessage(authToken: String, body: String, isAnonymous: Boolean, toInAppPhoneNo: String) {
        messageService.sendMessage(authToken, body, isAnonymous, toInAppPhoneNo)
    }

    override fun getSendMessageHistory(limit: Int, offset: Int, authToken: String): Call<List<MessageResponse>> {
        return messageService.getSendMessageHistory(limit, offset, authToken)
    }

    override fun getReceiveMessageHistory(limit: Int, offset: Int, authToken: String): Call<List<MessageResponse>> {
        return messageService.getReceiveMessageHistory(limit, offset, authToken)
    }

    override fun getMessage(messageId: String, authToken: String): Call<MessageResponse> {
        return messageService.getMessage(messageId, authToken)
    }

    companion object Factory {
        private var instance: MessageRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: MessageRepository().also { instance = it }
        }
    }
}
