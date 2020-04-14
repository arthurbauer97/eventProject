package com.arthur.eventsapp.view.adapters.listener

import com.arthur.eventsapp.data.domain.Event

interface ClickEventListener {
    fun onClickEvent(event: Event, position: Int)
}