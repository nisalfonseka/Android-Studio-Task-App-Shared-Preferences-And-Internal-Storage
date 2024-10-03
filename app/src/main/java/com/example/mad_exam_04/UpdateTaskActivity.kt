package com.example.mad_exam_04

import TasksDatabaseHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mad_exam_04.databinding.ActivityUpdateTaskBinding
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.TextView
import java.util.Calendar

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var tasksHelper: TasksDatabaseHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tasksHelper = TasksDatabaseHelper(this)

        taskId = intent.getIntExtra("task_id", -1)
        if (taskId == -1) {
            finish()
            return
        }

        // Retrieve task from internal storage
        val task = tasksHelper.getTaskByID(taskId)
        if (task == null) {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Set values to EditTexts
        binding.updateTitleEditText.setText(task.title)
        binding.updateTaskContentEditText.setText(task.content)
        binding.updateDateTextView.text = task.date
        binding.updateStartTimeTextView.text = task.startTime
        binding.updateEndTimeTextView.text = task.endTime

        // Save button listener
        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString().trim()
            val newContent = binding.updateTaskContentEditText.text.toString().trim()
            val newDate = binding.updateDateTextView.text.toString().trim()
            val newStartTime = binding.updateStartTimeTextView.text.toString().trim()
            val newEndTime = binding.updateEndTimeTextView.text.toString().trim()

            if (newTitle.isEmpty() || newContent.isEmpty() || newDate.isEmpty() || newStartTime.isEmpty() || newEndTime.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val updatedTask = Task(taskId, newTitle, newContent, newDate, newStartTime, newEndTime)
                tasksHelper.updateTask(updatedTask)

                // Schedule reminder for updated task
                tasksHelper.scheduleTaskReminder(updatedTask)

                Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Set listeners for date and time pickers
        binding.updateDateTextView.setOnClickListener {
            showDatePicker()
        }

        binding.updateStartTimeTextView.setOnClickListener {
            showTimePicker(binding.updateStartTimeTextView)
        }

        binding.updateEndTimeTextView.setOnClickListener {
            showTimePicker(binding.updateEndTimeTextView)
        }
    }

    // Function to show Date Picker Dialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            binding.updateDateTextView.text = date
        }, year, month, day)
        datePickerDialog.show()
    }

    // Function to show Time Picker Dialog
    private fun showTimePicker(targetTextView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            targetTextView.text = time
        }, hour, minute, true)  // 24-hour format
        timePickerDialog.show()
    }
}
