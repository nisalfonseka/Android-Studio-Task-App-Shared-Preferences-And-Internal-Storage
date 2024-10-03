package com.example.mad_exam_04

import TasksDatabaseHelper
import android.app.AlarmManager
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_exam_04.databinding.ActivityAllTasksBinding

class AllTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllTasksBinding
    private lateinit var tasksHelper: TasksDatabaseHelper
    private lateinit var tasksAdapter: TasksAdapter

    companion object {
        private const val REQUEST_CODE_NOTIFICATION_PERMISSION = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database helper and adapter
        tasksHelper = TasksDatabaseHelper(this)
        tasksAdapter = TasksAdapter(mutableListOf(), this)

        // Set up RecyclerView
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = tasksAdapter

        // Set up the add button click listener
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNewTaskActivity::class.java)
            startActivity(intent)
        }

        binding.timerButton.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }


        // Load tasks into the adapter
        loadTasks()
    }

    private fun loadTasks() {
        // Fetch and display tasks
        val tasks = tasksHelper.getAllTasks()
        tasksAdapter.updateTasks(tasks)
    }

    override fun onResume() {
        super.onResume()
        // Refresh tasks when returning to this activity
        loadTasks()

        // Handle permission requests and alarm scheduling
        checkNotificationPermission()
        checkAlarmPermission()
    }

    private fun checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATION_PERMISSION)
            }
        }
    }
}
