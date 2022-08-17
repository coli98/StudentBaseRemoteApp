package com.project.retrofitrxjava.data.repository

import androidx.paging.PagingData
import com.project.retrofitrxjava.data.paging.model.TaskPaging
import io.reactivex.rxjava3.core.Flowable

interface TaskRepositoryPaging {

    fun getAllTaskPaging():Flowable<PagingData<TaskPaging.Task>>
}