package com.project.retrofitrxjava.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.retrofitrxjava.data.paging.model.TaskPaging
import com.project.retrofitrxjava.databinding.TaskListViewBinding

class TaskPagingAdapter :
    PagingDataAdapter<TaskPaging.Task, TaskPagingAdapter.TaskViewHolderPaging>(
        diffCallback = diffUtil
    ) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TaskPaging.Task>() {
            override fun areItemsTheSame(
                oldItem: TaskPaging.Task,
                newItem: TaskPaging.Task
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TaskPaging.Task,
                newItem: TaskPaging.Task
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    class TaskViewHolderPaging(val binding: TaskListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: TaskPaging.Task) {

                binding.lbUserId.text = data.userId.toString()
                binding.lbTitle.text = data.title
                binding.lbBody.text = data.body
                binding.lbNote.text = data.note
                binding.lbStatus.text = data.status


        }
    }

    override fun onBindViewHolder(holder: TaskPagingAdapter.TaskViewHolderPaging, position: Int) {
        getItem(position)?.let {
            holder.onBind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskPagingAdapter.TaskViewHolderPaging {
        val binding =
            TaskListViewBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolderPaging(binding)
    }
}
