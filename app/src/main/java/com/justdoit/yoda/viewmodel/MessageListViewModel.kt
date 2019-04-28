package com.justdoit.yoda.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.justdoit.yoda.R
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.entity.MessageEntity
import com.justdoit.yoda.repository.MessageRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageListViewModel(app: Application) : AndroidViewModel(app) {
    private val messageRepository = MessageRepository.getInstance()
    val item = MutableLiveData<List<MessageEntity?>>()

    init {
        val authToken = SessionManager.instance.authToken
        authToken?.let {
            this@MessageListViewModel.getMessageList(null, null, it)
        }
    }

    fun getMessageList(limit: Int?, offset: Int?, authToken: String) {
        GlobalScope.launch {
            val messageResponse =
                messageRepository.getReceiveMessageHistory(limit, offset, authToken).await() ?: return@launch
            messageResponse.takeUnless { it.hasError }?.let {
                val response = it.body ?: return@let
                item.postValue(response.messages)
            } ?: run {
                item.postValue(null)
            }
        }
    }

    fun onClickFab(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_listFragment_to_sendFragment)
    }

}
