package com.justdoit.yoda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.entity.MessageEntity
import com.justdoit.yoda.entity.Source

class MessageListViewModel(app: Application) : AndroidViewModel(app) {
    var items: LiveData<List<MessageEntity>>? = null

    init {
        val fake = MutableLiveData<List<MessageEntity>>()
        val fakeItems = arrayListOf<MessageEntity>()
        for (i in 0..10) {
            val fakeItem = MessageEntity(i.toString(), Source.MOBILE, "userId", "message")
            fakeItems.add(fakeItem)
        }
        fake.value = fakeItems
        items = fake
    }

    fun load() {
        // todo 読み込んで`items`を更新する処理
    }
}