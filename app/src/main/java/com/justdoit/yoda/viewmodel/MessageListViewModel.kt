package com.justdoit.yoda.viewmodel

import android.app.Application
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.justdoit.yoda.R
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.entity.MessageEntity
import com.justdoit.yoda.entity.User
import com.justdoit.yoda.repository.MessageRepository
import com.justdoit.yoda.repository.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageListViewModel(app: Application) : AndroidViewModel(app) {
    private val messageRepository = MessageRepository.getInstance()

    var isLoading = ObservableBoolean()

    private val _item = MutableLiveData<List<MessageEntity?>>()
    val item: LiveData<List<MessageEntity?>> = _item

    init {
        callGetMessageList()
    }

    private fun callGetMessageList() {
        val authToken = SessionManager.instance.authToken
        authToken?.let {
            this@MessageListViewModel.getMessageList(null, null, it)
        }
    }

    private fun getMessageList(limit: Int?, offset: Int?, authToken: String) {
        GlobalScope.launch {
            val messageResponse =
                messageRepository.getReceiveMessageHistory(limit, offset, authToken).await() ?: return@launch
            messageResponse.takeUnless { it.hasError }?.let {
                val response = it.body ?: return@let
                _item.postValue(response.messages)
            } ?: run {
                _item.postValue(null)
            }
            onReady()
        }
    }

    fun onClickFab(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_listFragment_to_sendFragment)
    }

    fun onRefresh() {
        isLoading.set(true)
        callGetMessageList()
    }

    private fun onReady() {
        isLoading.set(false)
    }

    fun getMyPhoneNumber(): String {
        return "# ${SessionManager.instance.user!!.inAppPhoneNo}"
    }

}
