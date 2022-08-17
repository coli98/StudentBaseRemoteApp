package com.project.retrofitrxjava.data.paging.mapper

interface TaskPagingResponseMapper<Response,Model> {

    fun mapFromResponse(response: Response) : Model
}