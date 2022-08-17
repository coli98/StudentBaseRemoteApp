package com.project.retrofitrxjava.data.repository

import com.project.retrofitrxjava.data.remote.NetworkService
import com.project.retrofitrxjava.data.request.DeleteTaskRequest
import com.project.retrofitrxjava.data.request.TaskRequest
import com.project.retrofitrxjava.data.response.TaskResponse
import io.reactivex.rxjava3.core.Single

class TaskRepository (private val networkService: NetworkService) {

    fun getAllTask() : Single<TaskResponse> = networkService.getAllTask()

    fun searchTask(query: String) : Single<TaskResponse> = networkService.searchTask(query)

    fun addTask(taskRequest: TaskRequest) = networkService.addTask(taskRequest)

    fun editTask(taskRequest: TaskRequest) = networkService.editTask(taskRequest)

    fun deleteTask(deleteTaskRequest: DeleteTaskRequest) = networkService.deleteTask(deleteTaskRequest)
}