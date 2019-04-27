package com.justdoit.yoda

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.justdoit.yoda.repository.UserRepository
import com.justdoit.yoda.ui.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PushNotification: FirebaseMessagingService() {
    private val userRepository: UserRepository by lazy {
        UserRepository.getInstance()
    }

    private val notificationManager: NotificationManager by lazy {
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val channelId = "yoda_channel"
    private val title = "yoda"

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        GlobalScope.launch {
            val authToken = SessionManager.instance.authToken ?: return@launch
            val registerToken = p0 ?: return@launch
            Log.d(TAG, registerToken)
            this@PushNotification.userRepository.registerNotificationToken(authToken, registerToken).await()?.takeIf { it.hasError }?.let {
                Log.e(TAG, it.error.toString())
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage?.data ?: return
        val notification = remoteMessage.notification ?: return

        this.sendNotification(notification, data)
    }

    private fun sendNotification(remoteNotification: RemoteMessage.Notification, data: Map<String, String>) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.getNotificationBuilder(this.channelId, this.title)
        } else {
            NotificationCompat.Builder(this, this.channelId)
        }.apply {
            this.setContentTitle(remoteNotification.title)
            this.setContentText(remoteNotification.body)
            this.setSmallIcon(R.drawable.ic_launcher_foreground)
            this.setSound(defaultSoundUri)
            this.setContentIntent(pendingIntent)
            this.setWhen(System.currentTimeMillis())
            this.setAutoCancel(true)
            this.setDefaults(Notification.DEFAULT_ALL)
            this.setPriority(NotificationManager.IMPORTANCE_HIGH)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationBuilder(channelId: String, title: String): NotificationCompat.Builder {
        val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
        return NotificationCompat.Builder(this.applicationContext, channelId)
    }

    companion object {
        const val TAG = "PushNotification"
    }
}