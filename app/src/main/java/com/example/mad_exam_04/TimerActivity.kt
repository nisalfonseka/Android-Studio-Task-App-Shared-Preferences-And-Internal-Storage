package com.example.mad_exam_04

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_exam_04.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    private var handler = Handler()

    private var isRunning = false
    private var pauseOffset: Long = 0L
    private var startTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start button listener
        binding.startButton.setOnClickListener {
            if (!isRunning) {
                startTime = SystemClock.elapsedRealtime() - pauseOffset
                handler.post(runnable)
                isRunning = true
            }
        }

        // Pause button listener
        binding.pauseButton.setOnClickListener {
            if (isRunning) {
                pauseOffset = SystemClock.elapsedRealtime() - startTime
                handler.removeCallbacks(runnable)
                isRunning = false
            }
        }

        // Stop button listener
        binding.stopButton.setOnClickListener {
            if (isRunning) {
                val elapsedTime = SystemClock.elapsedRealtime() - startTime
                handler.removeCallbacks(runnable)
                binding.timerText.text = formatTime(elapsedTime)
                binding.timeWorkedText.text = "You worked ${formatWorkedTime(elapsedTime)}"
                resetTimer()
            } else if (pauseOffset > 0L) {
                // Handle case when the timer was paused and then stopped
                binding.timerText.text = formatTime(pauseOffset)
                binding.timeWorkedText.text = "You worked ${formatWorkedTime(pauseOffset)}"
                resetTimer()
            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            val currentTime = SystemClock.elapsedRealtime() - startTime
            binding.timerText.text = formatTime(currentTime)
            handler.postDelayed(this, 10) // Update every 10 milliseconds
        }
    }

    // Method to format time for the timer display
    private fun formatTime(timeInMillis: Long): String {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        val milliseconds = (timeInMillis % 1000) / 10 // Displaying 2-digit milliseconds
        return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
    }

    // Method to format time for the "You worked" message
    private fun formatWorkedTime(timeInMillis: Long): String {
        val hours = (timeInMillis / 1000) / 3600
        val minutes = ((timeInMillis / 1000) % 3600) / 60
        val seconds = (timeInMillis / 1000) % 60

        return when {
            hours > 0 -> String.format("%d hours, %d minutes, %d seconds", hours, minutes, seconds)
            minutes > 0 -> String.format("%d minutes, %d seconds", minutes, seconds)
            else -> String.format("%d seconds", seconds)
        }
    }

    // Reset timer after stopping
    private fun resetTimer() {
        isRunning = false
        pauseOffset = 0L
    }
}
