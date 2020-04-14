package com.arthur.eventsapp.data.domain

data class Event(
    val city: String,
    val coordinates: Coordinates,
    val date: Int,
    val description: String,
    val id: Long,
    val localName: String,
    val name: String,
    val photo: String
)