package com.justdoit.yoda.api

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.NotificationManager
import android.R
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.justdoit.yoda.ui.MainActivity



class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        // TODO 取得したInstanceIDをサーバーにわたす

        Log.d("firebase", token)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        val from = message?.from
        val notification = message?.notification
        val data = message?.data

        Log.d("firebase", "from: $from")
        Log.d("firebase", "data: $data")

        val notificationTitle = notification?.title
        val notificationBody = notification?.body

        Log.d("firebase", "title: $notificationTitle")
        Log.d("firebase", "body: $notificationBody")

        if (notificationTitle != null && notificationBody != null) {
            sendNotification(notificationTitle, notificationBody)
        }
    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setContentTitle(title)
            .setSubText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        notificationManager.notify(0, notificationBuilder.build())
    }

}
