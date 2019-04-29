package com.justdoit.yoda

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object GeneralSystem {
    @SuppressLint("SimpleDateFormat")
    fun parseFromISO8601(dateString: String): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'")
        val dt = df.parse(dateString)
        val df2 = SimpleDateFormat("yyyy/MM/dd HH:mm")
        return df2.format(dt)
    }
}