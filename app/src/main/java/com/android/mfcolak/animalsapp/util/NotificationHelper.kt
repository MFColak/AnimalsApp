package com.android.mfcolak.animalsapp.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.mfcolak.animalsapp.R
import com.android.mfcolak.animalsapp.view.MainActivity

class NotificationHelper(val context: Context) {

    private val CHANNEL_ID = "Animals"
    private val NOTIFICATION_ID = 123


    @SuppressLint("UnspecifiedImmutableFlag")
    fun createAnimalNotification() {
        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java).apply {

            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val iconBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.animal)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setLargeIcon(iconBitmap)
            .setContentTitle("Animals retrieved")
            .setContentText("This notification has some content")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(iconBitmap)
                    .bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }


    private fun createNotificationChannel() {

        val name = CHANNEL_ID
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT     //importance level
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}