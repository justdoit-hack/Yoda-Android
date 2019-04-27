package com.justdoit.yoda

import android.content.Context
import com.justdoit.yoda.entity.User

class SessionManager private constructor() {
    private val authTokenKey = "authToken"
    private val appName = "yoda"
    private val prefs = Yoda.instance.getSharedPreferences(this.appName, Context.MODE_PRIVATE)

    private object Holder {
        val INSTANCE = SessionManager()
    }

    companion object {
        val instance: SessionManager by lazy { Holder.INSTANCE }
    }

    var user: User? = null
    var authToken: String?
    set(value) {
        this.prefs.edit().putString(this.authTokenKey, value).apply()
    }
    get() {
        return this.prefs.getString(this.authTokenKey, null)
    }


    fun login(user: User) {
        this.user = user
        this.authToken = user.authToken ?: return
    }

    fun isLogin(): Boolean = this.user != null
    fun isRegistered(): Boolean = this.authToken != null

    fun logout() {
        this.user = null
        this.authToken = null
    }
}