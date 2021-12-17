package com.shiroumi.shiroplayer.arch.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.shiroumi.shiroplayer.R
import com.shiroumi.shiroplayer.activity.MainActivity

private const val CHANNEL_ID = "com.shiroumi.shiroplayer.notification"
private const val CHANNEL_NAME = "shiro_player"
private const val FOREGROUND_ID = 999

abstract class BaseService : Service() {
    lateinit var notification: Notification

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("music", "onStartCommand")

        val notificationBuilder: Notification.Builder

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder = Notification.Builder(applicationContext)
        } else {
            notificationBuilder = Notification.Builder(applicationContext, CHANNEL_ID)
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        notification = notificationBuilder
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.app_name))
            .setContentIntent(pendingIntent)
            .build()

        startForeground(FOREGROUND_ID, notification)

        return super.onStartCommand(intent, flags, startId)
    }
}