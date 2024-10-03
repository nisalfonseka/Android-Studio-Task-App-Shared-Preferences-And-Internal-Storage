import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import com.example.mad_exam_04.Task
import com.example.mad_exam_04.TaskReminderReceiver
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type
import java.util.Locale

class TasksDatabaseHelper(private val appContext: Context) {

    private val FILE_NAME = "user_tasks.json"
    private val gson = Gson()

    // Helper function to get the file where tasks will be saved
    private fun getFile(): File {
        return File(appContext.filesDir, FILE_NAME)
    }

    // Helper function to read tasks from internal storage
    private fun readTasksFromFile(): MutableList<Task> {
        val file = getFile()
        return if (file.exists()) {
            FileReader(file).use { reader ->
                val taskType: Type = object : TypeToken<MutableList<Task>>() {}.type
                gson.fromJson(reader, taskType) ?: mutableListOf()
            }
        } else {
            mutableListOf()
        }
    }

    // Helper function to write tasks to internal storage
    private fun writeTasksToFile(tasks: List<Task>) {
        val file = getFile()
        FileWriter(file).use { writer ->
            gson.toJson(tasks, writer)
        }
    }

    // Insert a new task and save it to the file
    fun insertTask(task: Task): Long {
        val tasks = readTasksFromFile()
        task.id = (tasks.maxByOrNull { it.id }?.id ?: 0) + 1  // Generate new ID
        tasks.add(task)
        writeTasksToFile(tasks)
        appContext.sendBroadcast(Intent("NEW_TASK_ADDED")) // Broadcast for widget or UI updates
        scheduleTaskReminder(task)
        return task.id.toLong()
    }

    // Update an existing task and save it to the file
    fun updateTask(task: Task) {
        val tasks = readTasksFromFile()
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
            writeTasksToFile(tasks)
            scheduleTaskReminder(task)
        } else {
            Log.e("TasksDatabaseHelper", "Task with ID ${task.id} not found.")
        }
    }

    // Get all tasks
    fun getAllTasks(): List<Task> {
        return readTasksFromFile()
    }

    // Get a task by ID
    fun getTaskByID(taskId: Int): Task? {
        return readTasksFromFile().find { it.id == taskId }
    }

    // Delete a task
    fun deleteTask(taskId: Int) {
        val tasks = readTasksFromFile()
        val updatedTasks = tasks.filter { it.id != taskId }
        writeTasksToFile(updatedTasks)
    }

    // Convert date and time to milliseconds
    private fun getTimeInMillis(date: String, time: String): Long {
        val format = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val dateTime = "$date $time"
        return format.parse(dateTime)?.time ?: System.currentTimeMillis()
    }

    // Schedule a reminder for the task using AlarmManager
    fun scheduleTaskReminder(task: Task) {
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(appContext, TaskReminderReceiver::class.java).apply {
            putExtra("task_title", task.title)
            putExtra("task_content", task.content)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Combine date and time into a Calendar object
        val calendar = Calendar.getInstance()

        // Parse the task date (assumes format "dd/MM/yyyy")
        val dateParts = task.date.split("/")
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt() - 1 // Calendar month is zero-based
        val year = dateParts[2].toInt()

        // Parse the task start time (assumes format "HH:mm")
        val timeParts = task.startTime.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        // Set calendar to the task's start date and time
        calendar.set(year, month, day, hour, minute, 0) // Set seconds to 0

        // Get the time in milliseconds
        val triggerTime = calendar.timeInMillis

        // If the trigger time is in the past, notify the user
        if (triggerTime > System.currentTimeMillis()) {
            // Set the alarm to go off at the calculated future time
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            Toast.makeText(appContext, "Start time has already passed", Toast.LENGTH_SHORT).show()
        }
    }
}
