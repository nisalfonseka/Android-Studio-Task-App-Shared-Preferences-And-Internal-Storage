package com.example.mad_exam_04

import TasksDatabaseHelper
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            // Update each widget instance
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.task_widget_provider)

        // Fetch upcoming tasks from the TasksDatabaseHelper
        val tasksHelper = TasksDatabaseHelper(context)
        val upcomingTasks = tasksHelper.getAllTasks().filter {
            val taskDateMillis = getTimeInMillis(it.date, it.startTime)
            taskDateMillis > System.currentTimeMillis() // Only upcoming tasks
        }

        // Update the widget title with the number of upcoming tasks
        views.setTextViewText(R.id.widget_title, "Today's Tasks (${upcomingTasks.size})")

        // Clear previous task views
        views.removeAllViews(R.id.task1)

        // For each upcoming task, dynamically add its title as a TextView
        for (task in upcomingTasks) {
            // Create a new RemoteViews for each task title
            val taskView = RemoteViews(context.packageName, android.R.layout.simple_list_item_1)
            taskView.setTextViewText(android.R.id.text1, task.title)  // Set the task title

            // Add the task view to the LinearLayout (task1)
            views.addView(R.id.task1, taskView)
        }

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getTimeInMillis(date: String, time: String): Long {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val dateTime = "$date $time"
        return format.parse(dateTime)?.time ?: System.currentTimeMillis()
    }



}