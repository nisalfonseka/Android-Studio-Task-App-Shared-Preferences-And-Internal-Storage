package com.example.mad_exam_04

import TasksDatabaseHelper
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private var tasks: MutableList<Task>, context: Context) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private val tasksHelper: TasksDatabaseHelper = TasksDatabaseHelper(context)

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tasks_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.title
        holder.contentTextView.text = task.content
        holder.dateTextView.text = "Date: ${task.date}"
        holder.timeTextView.text = "Time: ${task.startTime} - ${task.endTime}"

        // Handle the update button click event
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateTaskActivity::class.java).apply {
                putExtra("task_id", task.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        // Handle the delete button click event
        holder.deleteButton.setOnClickListener {
            tasksHelper.deleteTask(task.id)
            refreshData() // Refresh data after deletion
            Toast.makeText(holder.itemView.context, "Task Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    // Refreshes the data and notifies the RecyclerView to update the UI
    fun refreshData() {
        tasks.clear()
        tasks.addAll(tasksHelper.getAllTasks()) // Fetch the latest tasks
        notifyDataSetChanged()
    }

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
}
