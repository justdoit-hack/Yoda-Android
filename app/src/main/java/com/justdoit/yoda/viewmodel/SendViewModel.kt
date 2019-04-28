package com.justdoit.yoda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.justdoit.yoda.repository.MessageRepository

class SendViewModel(app: Application): AndroidViewModel(app) {
    private val messageRepository = MessageRepository.getInstance()


}