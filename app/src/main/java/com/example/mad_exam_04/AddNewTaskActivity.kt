package com.example.mad_exam_04

import TasksDatabaseHelper
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mad_exam_04.databinding.ActivityAddNewTaskBinding
import java.util.Calendar

class AddNewTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewTaskBinding
    private lateinit var tasksHelper: TasksDatabaseHelper
    private var selectedDate: String? = null
    private var startTime: String? = null
    private var endTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tasksHelper = TasksDatabaseHelper(this)  // Initialize TasksDatabaseHelper

        // Handle Save Button
        binding.saveButton.setOnClickListener {
            val title = binding.taskTitleEditText.text.toString()
            val content = binding.taskContentEditText.text.toString()

            if (title.isEmpty() || content.isEmpty() || selectedDate == null || startTime == null || endTime == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new task with the input values
            val task = Task(0, title, content, selectedDate!!, startTime!!, endTime!!)

            // Insert task into internal storage
            tasksHelper.insertTask(task)

            // Schedule the reminder for the task
            tasksHelper.scheduleTaskReminder(task)

            // Finish the activity and show success message
            finish()  // Close the activity
            Toast.makeText(this, "New Task Saved", Toast.LENGTH_SHORT).show()
        }

        // Date Picker Logic
        binding.taskDateTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.taskDateTextView.text = selectedDate
            }, year, month, day)
            datePickerDialog.show()
        }

        // Start Time Picker Logic
        binding.startTimeTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                startTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.startTimeTextView.text = startTime
            }, hour, minute, true)
            timePickerDialog.show()
        }

        // End Time Picker Logic
        binding.endTimeTextView.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                endTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.endTimeTextView.text = endTime
            }, hour, minute, true)
            timePickerDialog.show()
        }
    }
}
