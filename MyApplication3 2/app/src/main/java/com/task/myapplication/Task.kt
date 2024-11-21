package com.task.myapplication

data class Task(
    val id: Int = 0,
    val name: String,
    val date: String,
    val time: String,
    val description: String,
    val priority: String // High, Medium, or Low
)
