package com.arthur.eventsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val debugMessage: String,

    @SerializedName("showUserMessage")
    val showUserMessage: String
)