package com.task.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDatabaseHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        dbHelper = TaskDatabaseHelper(this)

        // Retrieve views
        val detailName: TextView = findViewById(R.id.detailName)
        val detailDate: TextView = findViewById(R.id.detailDate)
        val detailTime: TextView = findViewById(R.id.detailTime)
        val detailDescription: TextView = findViewById(R.id.detailDescription)
        val detailPriority: TextView = findViewById(R.id.detailPriority)
        val deleteButton: ImageView = findViewById(R.id.delete)
        // Initialize back ImageView and set onClickListener
        val backImageView: ImageView = findViewById(R.id.back)
        backImageView.setOnClickListener {
            // Go back to MainActivity
            finish() // Closes current activity and returns to the previous activity in the stack
            // Alternatively, you can use an explicit intent if needed:
            // val intent = Intent(this, MainActivity::class.java)
            // startActivity(intent)
        }
        // Get task details from intent extras
        taskId = intent.getIntExtra("taskId", -1)
        val taskName = intent.getStringExtra("taskName") ?: "N/A"
        val taskDate = intent.getStringExtra("taskDate") ?: "N/A"
        val taskTime = intent.getStringExtra("taskTime") ?: "N/A"
        val taskDescription = intent.getStringExtra("taskDescription") ?: "N/A"
        val taskPriority = intent.getStringExtra("taskPriority") ?: "N/A"

        // Populate the views with task details
        detailName.text = "Name: $taskName"
        detailDate.text = "Date: $taskDate"
        detailTime.text = "Time: $taskTime"
        detailDescription.text = "Description: $taskDescription"
        detailPriority.text = "Priority: $taskPriority"

        // Handle delete button click
        deleteButton.setOnClickListener {
            if (taskId != -1) {
                dbHelper.deleteTask(taskId)
                Toast.makeText(this, "Task deleted successfully!", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK) // Notify MainActivity about the deletion
                finish()
            } else {
                Toast.makeText(this, "Error: Task not found!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
