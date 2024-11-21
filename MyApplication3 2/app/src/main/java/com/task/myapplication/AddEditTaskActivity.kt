package com.task.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Spinner
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDatabaseHelper
    private var taskId: Int? = null

    // Declare the views
    private lateinit var taskNameInput: EditText
    private lateinit var taskDateInput: EditText
    private lateinit var taskTimeInput: EditText
    private lateinit var taskDescriptionInput: EditText
    private lateinit var taskPrioritySpinner: Spinner
    private lateinit var saveTaskButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_task)

        // Initialize the views
        taskNameInput = findViewById(R.id.taskNameInput)
        taskDateInput = findViewById(R.id.taskDateInput)
        taskTimeInput = findViewById(R.id.taskTimeInput)
        taskDescriptionInput = findViewById(R.id.taskDescriptionInput)
        taskPrioritySpinner = findViewById(R.id.taskPrioritySpinner)
        saveTaskButton = findViewById(R.id.saveTaskButton)
        // Initialize back ImageView and set onClickListener
        val backImageView: ImageView = findViewById(R.id.back)
        backImageView.setOnClickListener {
            // Go back to MainActivity
            finish() // Closes current activity and returns to the previous activity in the stack
            // Alternatively, you can use an explicit intent if needed:
            // val intent = Intent(this, MainActivity::class.java)
            // startActivity(intent)
        }

        dbHelper = TaskDatabaseHelper(this)

        // Check if editing an existing task
        taskId = intent.getIntExtra("taskId", -1)
        if (taskId != -1) {
            val task = dbHelper.getTaskById(taskId!!)
            task?.let {
                taskNameInput.setText(it.name)
                taskDateInput.setText(it.date)
                taskTimeInput.setText(it.time)
                taskDescriptionInput.setText(it.description)
                taskPrioritySpinner.setSelection(getPriorityIndex(it.priority))
            }
        }

        saveTaskButton.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val name = taskNameInput.text.toString()
        val date = taskDateInput.text.toString()
        val time = taskTimeInput.text.toString()
        val description = taskDescriptionInput.text.toString()
        val priority = taskPrioritySpinner.selectedItem.toString()

        if (name.isBlank() || date.isBlank() || time.isBlank()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val task = Task(
            id = taskId ?: 0,
            name = name,
            date = date,
            time = time,
            description = description,
            priority = priority
        )

        if (taskId == null || taskId == -1) {
            dbHelper.addTask(task) // Add new task
            Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()
        } else {
            dbHelper.updateTask(task) // Update existing task
            Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show()
        }

        setResult(RESULT_OK)
        finish()
    }

    private fun getPriorityIndex(priority: String): Int {
        return when (priority) {
            "High" -> 0
            "Medium" -> 1
            "Low" -> 2
            else -> 0
        }
    }
}
