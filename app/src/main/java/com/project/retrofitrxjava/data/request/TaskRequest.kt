package com.project.retrofitrxjava.data.request

import android.icu.text.CaseMap
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TaskRequest(
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("status")
    val status: String,

)
