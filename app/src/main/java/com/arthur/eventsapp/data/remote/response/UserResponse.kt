package com.arthur.eventsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("cpf")
    val cpf: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("photo")
    val photo: String
)