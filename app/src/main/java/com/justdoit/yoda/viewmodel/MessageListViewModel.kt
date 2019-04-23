package com.justdoit.yoda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.justdoit.yoda.entity.FakeApiEntity
import com.justdoit.yoda.repository.FakeRepository

class MessageListViewModel(app: Application) : AndroidViewModel(app) {
    val repo = FakeRepository.getInstance()
    val items: LiveData<List<FakeApiEntity?>>? by lazy {
        repo.getPosts()
    }

    init {
//        val fake = MutableLiveData<List<MessageEntity>>()
//        val fakeItems = arrayListOf<MessageEntity>()
//        for (i in 0..10) {
//            val fakeItem = MessageEntity(i.toString(), Source.MOBILE, "userId", "message")
//            fakeItems.add(fakeItem)
//        }
//        fake.value = fakeItems
//        items = fake
    }
}