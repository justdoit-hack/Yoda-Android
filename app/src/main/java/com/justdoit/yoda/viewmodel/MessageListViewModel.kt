package com.justdoit.yoda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.entity.MessageEntity
import com.justdoit.yoda.repository.MessageRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageListViewModel(app: Application) : AndroidViewModel(app) {
    private val messageRepository = MessageRepository.getInstance()

    fun getMessageList(limit: Int?, offset: Int?, authToken: String): LiveData<List<MessageEntity?>> {
        val data = MutableLiveData<List<MessageEntity?>>()

        GlobalScope.launch {
            val messageResponse =
                messageRepository.getReceiveMessageHistory(limit, offset, authToken).await() ?: return@launch
            messageResponse.takeUnless { it.hasError }?.let {
                val response = it.body ?: return@let
                data.postValue(response.messages)
            } ?: run {
                data.postValue(null)
            }
        }
        return data
    }

}
