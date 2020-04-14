package com.arthur.eventsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("authToken")
    val authToken: String
)