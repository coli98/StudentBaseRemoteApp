package com.project.retrofitrxjava.data.remote

import com.project.retrofitrxjava.data.request.DeleteTaskRequest
import com.project.retrofitrxjava.data.request.TaskRequest
import com.project.retrofitrxjava.data.response.TaskResponse
import com.project.retrofitrxjava.data.response.TaskResponsePaging
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface NetworkService {

    @Headers(Endpoint.HEADER_ACCEPT)
    @GET(Endpoint.GET_ALL_TASK)
    fun getAllTask():Single<TaskResponse>

    @Headers(Endpoint.HEADER_ACCEPT)
    @GET("${Endpoint.SEARCH_TASK}{query}")
    fun searchTask(
        @Path("query") query: String) : Single<TaskResponse>



    @Headers(Endpoint.HEADER_ACCEPT)
    @POST(Endpoint.ADD_TASK)
    fun addTask(
        @Body taskRequest: TaskRequest
    ):Single<TaskResponse.TaskResponseItem>

    @Headers(Endpoint.HEADER_ACCEPT)
    @POST(Endpoint.EDIT_TASK)
    fun editTask(
        @Body taskRequest: TaskRequest
    ):Single<TaskResponse.TaskResponseItem>

    @Headers(Endpoint.HEADER_ACCEPT)
    @POST(Endpoint.DELETE_TASK)
    fun deleteTask(
        @Body deleteTaskRequest: DeleteTaskRequest
    ):Single<TaskResponse.TaskResponseItem>

    @Headers(Endpoint.HEADER_ACCEPT)
    @GET(Endpoint.GET_ALL_TASK_PAGING)
    fun  getAllTaskPaging(@Query("page") pageNumber: Int) : Single<TaskResponsePaging>

}