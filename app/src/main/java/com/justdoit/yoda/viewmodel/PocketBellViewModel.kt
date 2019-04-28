package com.justdoit.yoda.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel

class PocketBellViewModel(application: Application) : AndroidViewModel(application) {
    val statusDisplay: ObservableField<DisplayInfo> = ObservableField()
    private var page = 0

    val displays = listOf(
        DisplayInfo("頑張れば不可能は無い", Color.parseColor("#EFEDE4")),
        DisplayInfo("最強になれ", Color.parseColor("#EFEDE4"))
    )

    init {
        val statusInit = DisplayInfo("カクニンチュウ", Color.parseColor("#EFEDE4"))
        statusDisplay.set(statusInit)
        page = 0
    }

    fun clickLeftBtn() {
        page--
        if (page < 0) {
            page = displays.size - 1
        }
        changePage(page)
    }

    fun clickRightBtn() {
        page++
        if (page >= displays.size) {
            page = 0
        }
        changePage(page)
    }

    fun changePage(page: Int) {
        statusDisplay.set(displays[page])
    }

    data class DisplayInfo(
        val text: String,
        val colorBG: Int
    )
}