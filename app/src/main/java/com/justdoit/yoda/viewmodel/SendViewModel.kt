package com.justdoit.yoda.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.SessionManager
import com.justdoit.yoda.repository.MessageRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SendViewModel(app: Application): AndroidViewModel(app) {
    private val messageRepository = MessageRepository.getInstance()
    val finishPostEvent = MutableLiveData<HashMap<String, String>>()

    fun postMessage(toInAppPhoneNo: String ,body: String) {
        val authToken = SessionManager.instance.authToken ?: return
        GlobalScope.launch {
            this@SendViewModel.messageRepository.sendMessage(
                authToken,
                body,
                false,
                toInAppPhoneNo
            ).await()?.let {
                if (!it.hasError) {
                    this@SendViewModel.finishPostEvent.postValue(hashMapOf("success" to "true"))
                } else {
                    Log.e(TAG, it.error.toString())
                    when (it.error?.code) {
                        "1007" -> this@SendViewModel.finishPostEvent.postValue(hashMapOf("success" to "false", "errorMessage" to "送信先ユーザーが見つかりませんでした..."))
                        else -> this@SendViewModel.finishPostEvent.postValue(hashMapOf("success" to "false", "errorMessage" to "送信に失敗しました"))
                    }
                }
            } ?: run {
                Log.e(TAG, "SERVER NOT RESPONSE")
                this@SendViewModel.finishPostEvent.postValue(hashMapOf("success" to "false"))
            }
        }
    }

    companion object {
        private const val TAG = "SendViewModel"
    }
}