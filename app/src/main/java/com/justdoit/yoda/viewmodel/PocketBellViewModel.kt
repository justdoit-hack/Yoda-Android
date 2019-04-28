package com.justdoit.yoda.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel

class PocketBellViewModel(application: Application) : AndroidViewModel(application) {
    val statusDisplay: ObservableField<DisplayInfo> = ObservableField()

    init {
        val statusInit = DisplayInfo("カクニンチュウ", Color.parseColor("#EFEDE4"))
        statusDisplay.set(statusInit)
    }

    data class DisplayInfo(
        val text: String,
        val colorBG: Int
    )
}