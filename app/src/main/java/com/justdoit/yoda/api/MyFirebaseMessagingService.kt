package com.justdoit.yoda.api

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        // TODO 取得したInstanceIDをサーバーにわたす

        Log.d("firebase", token)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        val from = message?.from
        val data = message?.data

        Log.d("firebase", "from: $from")
        Log.d("firebase", "data: $data")
    }



}
