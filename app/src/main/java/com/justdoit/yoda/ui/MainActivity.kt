package com.justdoit.yoda.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.justdoit.yoda.R
import com.justdoit.yoda.repository.MessageRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val authToken = "57b77dc3fa134abbbce2d529ff65f11b"
        val body = "112233"
        val isAnonymous = false
        val toInAppPhoneNo = "0108792"

        // messages/create のテスト
//        GlobalScope.launch {
//            val messageRepository = MessageRepository.getInstance()
//            val messageRes =
//                messageRepository.sendMessage(authToken, body, isAnonymous, toInAppPhoneNo).await() ?: return@launch
//            messageRes.takeUnless { it.hasError }?.let {
//                val messageResponse = it.body ?: return@let
//                Log.d("ID_TOKEN", messageResponse.ok.toString())
//            } ?: run {
//                Log.e("TOKEN_REGISTER_ERROR", messageRes.error.toString())
//            }
//        }

        // messages/history/send のテスト
//        GlobalScope.launch {
//            val messageRepository = MessageRepository.getInstance()
//            val messageRes =
//                messageRepository.getSendMessageHistory(50, 50, authToken).await() ?: return@launch
//            Log.d("MESSAGE", messageRes.toString())
//            messageRes.takeUnless { it.hasError }?.let {
//                val messageResponse = it.body ?: return@let
//                Log.d("MESSAGE", messageResponse.message.toString())
//            } ?: run {
//                Log.e("TOKEN_REGISTER_ERROR", messageRes.error.toString())
//            }
//        }

        // messages/history/receive のテスト
        GlobalScope.launch {
            val messageRepository = MessageRepository.getInstance()
            val messageRes =
                messageRepository.getReceiveMessageHistory(50, 50, authToken).await() ?: return@launch
            Log.d("MESSAGE", messageRes.toString())
            messageRes.takeUnless { it.hasError }?.let {
                val messageResponse = it.body ?: return@let
                Log.d("MESSAGE", messageResponse.toString())
            } ?: run {
                Log.e("TOKEN_REGISTER_ERROR", messageRes.error.toString())
            }
        }

        // messages/:messageId のテスト
//        GlobalScope.launch {
//            val messageRepository = MessageRepository.getInstance()
//            val messageRes =
//                messageRepository.getMessage("", authToken).await() ?: return@launch
//            Log.d("MESSAGE", messageRes.toString())
//            messageRes.takeUnless { it.hasError }?.let {
//                val messageResponse = it.body ?: return@let
//                Log.d("MESSAGE", messageResponse.message.toString())
//            } ?: run {
//                Log.e("TOKEN_REGISTER_ERROR", messageRes.error.toString())
//            }
//        }


    }

}
