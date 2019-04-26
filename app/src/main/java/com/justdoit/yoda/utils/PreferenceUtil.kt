package com.justdoit.yoda.utils

import android.content.Context
import android.preference.PreferenceManager

class PreferenceUtil(context: Context) {
    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    var authTokenPref: String? = preference.getString("authToken", null)
        set(value) {
            preference.edit().putString("authToken", value).apply()
            field = value
        }
}
