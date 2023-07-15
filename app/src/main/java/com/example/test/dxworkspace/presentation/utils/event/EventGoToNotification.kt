package com.example.test.dxworkspace.presentation.utils.event

class EventGoToNotification(val type: Int, var message: List<String>? = null) : EventBus.Event() {
    companion object {
        const val DETAIL_TASK = 1
        const val DETAIL_REQUEST = 2
        const val DIALOG_DETAIL = 3
    }
}