package com.project.retrofitrxjava.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.project.retrofitrxjava.data.paging.mapper.TaskPagingResponseMapper
import com.project.retrofitrxjava.data.paging.model.TaskPaging
import com.project.retrofitrxjava.data.remote.NetworkService
import com.project.retrofitrxjava.data.response.TaskResponsePaging
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class TaskPagingSource(
    private val networkService: NetworkService
) : RxPagingSource<Int, TaskPaging.Task>(),
    TaskPagingResponseMapper<TaskResponsePaging, TaskPaging> {
    override fun getRefreshKey(state: PagingState<Int, TaskPaging.Task>): Int? {
        return state.anchorPosition
    }

    override fun loadSingle(params: LoadParams<Int>): Single<PagingSource.LoadResult<Int, TaskPaging.Task>> {
        val currentPage = params.key ?: 1
        return networkService.getAllTaskPaging(currentPage)
            .subscribeOn(Schedulers.io())
            .map { mapFromResponse(it) }
            .map { loadResult(data = it, currentPage) }
            .onErrorReturn { LoadResult.Error(it) }

    }

    private fun loadResult(data:TaskPaging, currentPage:Int):LoadResult<Int, TaskPaging.Task> =
        LoadResult.Page(
            data = data.tasks,
            prevKey = if(currentPage==1)null else currentPage -1,
            nextKey = if(currentPage == data.lastPage) null else currentPage + 1

        )


    override fun mapFromResponse(response: TaskResponsePaging): TaskPaging {
        return with(response){
            TaskPaging(
                totalPage = lastPage,
                currentPage = currentPage,
                tasks = tasks.map{
                    TaskPaging.Task(
                        id = it.id,
                        userId = it.userId,
                        title= it.title,
                        body = it.body,
                        note = it.note,
                        status = it.status,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt
                    )
                }
            )
        }
    }

}