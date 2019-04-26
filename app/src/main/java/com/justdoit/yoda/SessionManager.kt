package com.justdoit.yoda

import android.content.Context
import com.justdoit.yoda.entity.User
import com.justdoit.yoda.ui.MainActivity

class SessionManager private constructor(activity: MainActivity) {
    private val authTokenKey = "authToken"
    private val prefs = activity.getPreferences(Context.MODE_PRIVATE)

    var authToken: String?
    set(value) {
        this.prefs.edit().putString(this.authTokenKey, value).apply()
    }
    get() {
        val token = this.prefs.getString(this.authTokenKey, "")
        return if (token != "") token else null
    }
    var user: User? = null

    companion object {
        private var instance: SessionManager? = null
        fun getInstanc(activity: MainActivity): SessionManager {
            return this.instance ?: SessionManager(activity)
        }
    }

    fun login(user: User) {
        this.user = user
        this.authToken = user.authToken ?: return
    }

    fun isLogin(): Boolean = this.user != null

    fun logout() {
        this.user = null
        this.authToken = ""
    }
}