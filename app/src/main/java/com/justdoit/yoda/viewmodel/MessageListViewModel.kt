package com.justdoit.yoda.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.justdoit.yoda.R
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.entity.MessageEntity
import com.justdoit.yoda.repository.MessageRepository
import com.justdoit.yoda.repository.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageListViewModel(app: Application) : AndroidViewModel(app) {
    private val messageRepository = MessageRepository.getInstance()
    private val userRepository = UserRepository.getInstance()

    private val _item = MutableLiveData<List<MessageEntity?>>()
    val item: LiveData<List<MessageEntity?>> = _item

    private val _myInAppPhoneNo = MutableLiveData<String>()
    val myInAppPhoneNo: LiveData<String> = _myInAppPhoneNo

    init {
        val authToken = SessionManager.instance.authToken
        authToken?.let {
            this@MessageListViewModel.getMessageList(null, null, it)
            getMyUserData(authToken)
        }
    }

    fun getMessageList(limit: Int?, offset: Int?, authToken: String) {
        GlobalScope.launch {
            val messageResponse =
                messageRepository.getReceiveMessageHistory(limit, offset, authToken).await() ?: return@launch
            messageResponse.takeUnless { it.hasError }?.let {
                val response = it.body ?: return@let
                _item.postValue(response.messages)
            } ?: run {
                _item.postValue(null)
            }
        }
    }

    private fun getMyUserData(authToken: String) {
        GlobalScope.launch {
            val userResponse = userRepository.fetchUser(authToken).await() ?: return@launch
            userResponse.takeUnless { it.hasError }?.let {
                val response = it.body ?: return@let
                _myInAppPhoneNo.postValue(response.user.inAppPhoneNo)
            } ?: run {
                _myInAppPhoneNo.postValue(null)
            }
        }
    }

    fun onClickFab(view: View) {
        Navigation.findNavController(view).navigate(R.id.action_listFragment_to_sendFragment)
    }

}
