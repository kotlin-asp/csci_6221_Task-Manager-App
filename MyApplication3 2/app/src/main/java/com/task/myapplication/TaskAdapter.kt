package com.task.myapplication

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: List<Task>,
    private val onItemClick: (Task) -> Unit,
    private val onUpdateClick: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val taskName: TextView = view.findViewById(R.id.taskName)
        private val taskDate: TextView = view.findViewById(R.id.taskDate)
        private val taskTime: TextView = view.findViewById(R.id.taskTime)
        private val taskDescription: TextView = view.findViewById(R.id.taskDescription)
        private val taskPriority: TextView = view.findViewById(R.id.taskPriority)
        private val updateButton: ImageView = view.findViewById(R.id.updateButton)
        private val deleteButton: ImageView = view.findViewById(R.id.deleteButton)
        private val taskContainer: LinearLayout = view.findViewById(R.id.cotainer)

        fun bind(task: Task) {
            taskName.text = task.name
            taskDate.text = task.date
            taskTime.text = task.time
            taskDescription.text = task.description
            taskPriority.text = task.priority

            taskContainer.background = getPriorityBackground(task.priority)

            itemView.setOnClickListener { onItemClick(task) }
            updateButton.setOnClickListener { onUpdateClick(task) }
            deleteButton.setOnClickListener { onDeleteClick(task) }
        }

        private fun getPriorityBackground(priority: String): Drawable? {
            return when (priority) {
                "High" -> itemView.context.getDrawable(R.drawable.round)
                "Medium" -> itemView.context.getDrawable(R.drawable.round_blue)
                "Low" -> itemView.context.getDrawable(R.drawable.roundgreen)
                else -> itemView.context.getDrawable(R.drawable.round_et)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount() = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }
}
