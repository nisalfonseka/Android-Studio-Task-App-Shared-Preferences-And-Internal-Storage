package com.example.mad_exam_04

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class TaskReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra("task_id", -1)
        val taskTitle = intent.getStringExtra("task_title") ?: "No Title"
        val taskContent = intent.getStringExtra("task_content") ?: "No Content"

        // Create a notification manager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_reminders"

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create a pending intent to open the app when the notification is tapped
        val notificationIntent = Intent(context, AllTasksActivity::class.java).apply {
            putExtra("task_id", taskId) // Pass the task ID to the activity
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE
        )

        // Build and display the notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(taskTitle)
            .setContentText(taskContent)
            .setSmallIcon(R.drawable.ic_notification) // Replace with your notification icon
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(taskId, notification)
    }
}
