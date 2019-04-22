package com.justdoit.yoda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.entity.MessageEntity

class ListViewModel(app: Application) : AndroidViewModel(app) {
    val items: MutableLiveData<List<MessageEntity>> by lazy {
        MutableLiveData<List<MessageEntity>>()
    }

    fun load() {
        // todo 読み込んで`items`を更新する処理
    }
}