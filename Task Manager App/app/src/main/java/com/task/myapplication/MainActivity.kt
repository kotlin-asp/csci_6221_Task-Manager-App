package com.task.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDatabaseHelper
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private var taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize database helper
        dbHelper = TaskDatabaseHelper(this)

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerView)
        taskAdapter = TaskAdapter(
            taskList,
            { task ->
                // Handle item click (details activity)
                val intent = Intent(this, DetailsActivity::class.java).apply {
                    putExtra("taskId", task.id)
                    putExtra("taskName", task.name)
                    putExtra("taskDate", task.date)
                    putExtra("taskTime", task.time)
                    putExtra("taskDescription", task.description)
                    putExtra("taskPriority", task.priority)
                }
                startForResult.launch(intent)
            },
            { task ->
                // Handle update button click (edit activity)
                val intent = Intent(this, AddEditTaskActivity::class.java).apply {
                    putExtra("taskId", task.id)
                    putExtra("taskName", task.name)
                    putExtra("taskDate", task.date)
                    putExtra("taskTime", task.time)
                    putExtra("taskDescription", task.description)
                    putExtra("taskPriority", task.priority)
                }
                startForResult.launch(intent)
            },
            { task ->
                // Handle delete button click
                deleteTask(task)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // Load and display tasks
        updateTaskList()

        // Floating Action Button for adding new tasks
        findViewById<FloatingActionButton>(R.id.addTaskFab).setOnClickListener {
            val intent = Intent(this, AddEditTaskActivity::class.java)
            startForResult.launch(intent)
        }
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            updateTaskList()
        }
    }

    private fun updateTaskList() {
        val allTasks = dbHelper.getAllTasks()
        val sortedTasks = allTasks.sortedBy { task ->
            when (task.priority) {
                "High" -> 1
                "Medium" -> 2
                "Low" -> 3
                else -> 4
            }
        }
        taskAdapter.updateTasks(sortedTasks)
    }

    private fun deleteTask(task: Task) {
        dbHelper.deleteTask(task.id)
        updateTaskList()
    }
}
