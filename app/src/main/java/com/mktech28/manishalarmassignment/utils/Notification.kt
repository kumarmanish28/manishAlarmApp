package com.mktech28.manishalarmassignment.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.mktech28.manishalarmassignment.R

class Notification(
    val NotificationName: String = "My Alarm Notifications",
    val ChannelId: String = "my_alarm_notification_channel",
    val NotificationId: Int,
    var title: String,
    var description: String,
    var icon: Int = R.drawable.ic_alarm_add_24
) {

    fun displayNotification(context: Context) {
        val notificationManager = getNotificationManager(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(getNotificationChannel())
        }

        with(notificationManager) {
            notify(NotificationId, getNotificationBuilder(context).build())
        }
    }


    private fun getNotificationBuilder(context: Context): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, ChannelId).apply {
            setSmallIcon(icon)
            setContentTitle(title)
            setContentText(description)
            setPriority(NotificationCompat.PRIORITY_HIGH)
            setCategory(NotificationCompat.CATEGORY_ALARM)
            setOngoing(true)
        }


    }

    private fun getNotificationManager(context: Context): NotificationManager {
        return ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(): NotificationChannel {
        return NotificationChannel(
            ChannelId,
            NotificationName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = this@Notification.description
            setSound(null, null)
        }
    }


    companion object {

        fun cancelNotification(context: Context, notificationId: Int) {
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.cancel(notificationId)
        }
    }

}
