package com.project.retrofitrxjava.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.project.retrofitrxjava.R
import com.project.retrofitrxjava.data.response.TaskResponse

class TaskAdapter(
    private val editClickListener: (data: TaskResponse.TaskResponseItem) -> Unit,
    private val deleteClickListener: (data: TaskResponse.TaskResponseItem)->Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<TaskResponse.TaskResponseItem>() {
        override fun areItemsTheSame(
            oldItem: TaskResponse.TaskResponseItem,
            newItem: TaskResponse.TaskResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: TaskResponse.TaskResponseItem,
            newItem: TaskResponse.TaskResponseItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun setTaskList(newItem: ArrayList<TaskResponse.TaskResponseItem>) {
        asyncListDiffer.submitList(newItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_list_view, parent, false)
        return TaskViewHolder(view, editClickListener,deleteClickListener)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size


    class TaskViewHolder(
        itemViem: View,
        private val editClickListner: (data: TaskResponse.TaskResponseItem) -> Unit,
        private val deleteClickListener: (data: TaskResponse.TaskResponseItem)->Unit
    ) : RecyclerView.ViewHolder(itemViem) {

        private val userId = itemView.findViewById<TextView>(R.id.lbUserId)
        private val title = itemView.findViewById<TextView>(R.id.lbTitle)
        private val body = itemView.findViewById<TextView>(R.id.lbBody)
        private val note = itemView.findViewById<TextView>(R.id.lbNote)
        private val status = itemView.findViewById<TextView>(R.id.lbStatus)
        private val btnEdit = itemView.findViewById<ShapeableImageView>(R.id.btnEdit)
        private val btnDelete = itemView.findViewById<ShapeableImageView>(R.id.btnDelete)


        fun bind(data: TaskResponse.TaskResponseItem) {
            userId.text = data.userId.toString()
            title.text = data.title
            body.text = data.body
            note.text = data.note
            status.text = data.status

            btnEdit.setOnClickListener {
                editClickListner(data)
            }


            btnDelete.setOnClickListener {
                deleteClickListener(data)
            }


        }
    }

}