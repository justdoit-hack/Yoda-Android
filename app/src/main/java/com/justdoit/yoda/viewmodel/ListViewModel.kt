package com.justdoit.yoda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.justdoit.yoda.entity.YodaEntity

class ListViewModel(app: Application) : AndroidViewModel(app) {
    val items: MutableLiveData<List<YodaEntity>> by lazy {
        MutableLiveData<List<YodaEntity>>()
    }

    fun load() {
        // todo 読み込んで`items`を更新する処理
    }
}