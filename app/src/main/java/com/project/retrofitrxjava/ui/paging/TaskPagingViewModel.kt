package com.project.retrofitrxjava.ui.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.project.retrofitrxjava.data.paging.model.TaskPaging
import com.project.retrofitrxjava.data.repository.paging.TaskRepositoryPagingImpl
import io.reactivex.rxjava3.core.Flowable

class TaskPagingViewModel(
    private val repository: TaskRepositoryPagingImpl
) : ViewModel() {

    fun getAllTaskPaging(): Flowable<PagingData<TaskPaging.Task>>{
        return repository.getAllTaskPaging()
            .cachedIn(viewModelScope)
    }
}