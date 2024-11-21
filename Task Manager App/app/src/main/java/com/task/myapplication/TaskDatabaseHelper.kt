package com.task.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TaskPro.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_PRIORITY = "priority"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_TIME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_PRIORITY TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert a new task
    fun addTask(task: Task): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, task.name)
            put(COLUMN_DATE, task.date)
            put(COLUMN_TIME, task.time)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_PRIORITY, task.priority)
        }
        return db.insert(TABLE_NAME, null, values).also { db.close() }
    }

    // Retrieve all tasks
    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, "$COLUMN_PRIORITY DESC")
        cursor?.use {
            while (it.moveToNext()) {
                tasks.add(
                    Task(
                        id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                        name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                        date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE)),
                        time = it.getString(it.getColumnIndexOrThrow(COLUMN_TIME)),
                        description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        priority = it.getString(it.getColumnIndexOrThrow(COLUMN_PRIORITY))
                    )
                )
            }
        }
        db.close()
        return tasks
    }

    // Delete a task
    fun deleteTask(taskId: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(taskId.toString()))
        db.close()
    }
    fun getTaskById(taskId: Int): Task? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(taskId.toString()),
            null,
            null,
            null
        )
        var task: Task? = null
        cursor?.use {
            if (it.moveToFirst()) {
                task = Task(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE)),
                    time = it.getString(it.getColumnIndexOrThrow(COLUMN_TIME)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    priority = it.getString(it.getColumnIndexOrThrow(COLUMN_PRIORITY))
                )
            }
        }
        db.close()
        return task
    }

    fun updateTask(task: Task): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, task.name)
            put(COLUMN_DATE, task.date)
            put(COLUMN_TIME, task.time)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_PRIORITY, task.priority)
        }
        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(task.id.toString())).also {
            db.close()
        }
    }

}
