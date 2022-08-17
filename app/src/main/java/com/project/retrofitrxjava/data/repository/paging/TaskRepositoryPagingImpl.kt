package com.project.retrofitrxjava.data.repository.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.project.retrofitrxjava.data.paging.TaskPagingSource
import com.project.retrofitrxjava.data.paging.model.TaskPaging
import com.project.retrofitrxjava.data.repository.TaskRepositoryPaging
import com.project.retrofitrxjava.data.response.TaskResponsePaging
import io.reactivex.rxjava3.core.Flowable

class TaskRepositoryPagingImpl(
    private val taskPagingSource:TaskPagingSource
) : TaskRepositoryPaging{

    override fun getAllTaskPaging(): Flowable<PagingData<TaskPaging.Task>> {
        return Pager(
            config = pagingConfig(),
            pagingSourceFactory = {taskPagingSource}
        ).flowable
    }

    private fun pagingConfig(): PagingConfig{
        return PagingConfig(
            pageSize = 10,
            prefetchDistance = 20,
            enablePlaceholders = false,
            initialLoadSize = 30,
            maxSize = 50
        )
    }
}