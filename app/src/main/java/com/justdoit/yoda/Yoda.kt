package com.justdoit.yoda

import android.app.Application

class Yoda: Application() {
    companion object {
        lateinit var instance: Application private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
