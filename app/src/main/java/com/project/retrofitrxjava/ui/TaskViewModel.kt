package com.project.retrofitrxjava.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayout
import com.project.retrofitrxjava.MyApplication
import com.project.retrofitrxjava.data.repository.TaskRepository
import com.project.retrofitrxjava.data.request.DeleteTaskRequest
import com.project.retrofitrxjava.data.request.TaskRequest
import com.project.retrofitrxjava.data.response.TaskResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "TaskViewModel"
    }

    private val compositeDisposable = CompositeDisposable()
    private lateinit var taskRepository: TaskRepository

    val mTaskId: MutableLiveData<Long> = MutableLiveData()
    val mUserId: MutableLiveData<String> = MutableLiveData()
    val mTitle: MutableLiveData<String> = MutableLiveData()
    val mBody: MutableLiveData<String> = MutableLiveData()
    val mNote: MutableLiveData<String> = MutableLiveData()
    val mStatus: MutableLiveData<String> = MutableLiveData()

    val isError: MutableLiveData<String> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isEditSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isDeleted: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val taskList: MutableLiveData<ArrayList<TaskResponse.TaskResponseItem>> = MutableLiveData()

/*    init {
        taskRepository = TaskRepository((application as MyApplication).networkService)
        getAllTask()
    }*/

     fun getAllTask() {
        isLoading.value = true
        compositeDisposable.add(
            taskRepository.getAllTask()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        taskList.value = it
                        isLoading.value = false
                        taskList.value!!.forEach {
                            Log.d(TAG, it.toString())
                        }
                    },
                    {
                        isError.value = it.message
                        isLoading.value = false
                    }
                )
        )
    }

    fun searchTask(query:String): Single<TaskResponse>{
        return taskRepository.searchTask(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    fun addTask() {
        isLoading.value = true
        compositeDisposable.add(
            taskRepository.addTask(createTaskRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoading.value = false
                        getAllTask()
                        isSuccess.value = true

                    },
                    {
                        isError.value = it.message
                        isLoading.value = false
                    }
                )
        )
    }

    fun editTask() {
        isLoading.value = true

        compositeDisposable.add(
            taskRepository.editTask(createEditTaskRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(TAG, it.toString())
                        isLoading.value = false
                        getAllTask()
                        isEditSuccess.value = true
                    },
                    {
                        isError.value = it.message
                        isLoading.value = false
                    }
                )
        )
    }

    fun deleteTask() {
        isLoading.value = true
        compositeDisposable.add(
            taskRepository.deleteTask(createDeleteTaskRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        isLoading.value = false
                        Log.d(TAG, it.toString())
                        getAllTask()
                        isDeleted.value = true
                    },
                    {
                        isError.value = it.message
                        isLoading.value = false
                    }
                )
        )
    }

    private fun createDeleteTaskRequest(): DeleteTaskRequest {
        return DeleteTaskRequest(
            id = mTaskId.value!!,
            userId = mUserId.value.toString()
        )
    }

    private fun createEditTaskRequest(): TaskRequest {
        return TaskRequest(
            id = mTaskId.value!!,
            userId = mUserId.value.toString(),
            title = mTitle.value.toString(),
            body = mBody.value.toString(),
            note = mNote.value.toString(),
            status = mStatus.value.toString()
        )

    }

    private fun createTaskRequest(): TaskRequest {
        return TaskRequest(
            userId = mUserId.value.toString(),
            title = mTitle.value.toString(),
            body = mBody.value.toString(),
            note = mNote.value.toString(),
            status = mStatus.value.toString()
        )


    }
}