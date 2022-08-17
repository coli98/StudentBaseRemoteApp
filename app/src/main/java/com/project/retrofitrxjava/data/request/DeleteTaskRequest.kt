package com.project.retrofitrxjava.data.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DeleteTaskRequest(
    @SerializedName("id")
    val id: Long,
    @SerializedName("user_id")
    val  userId: String
)