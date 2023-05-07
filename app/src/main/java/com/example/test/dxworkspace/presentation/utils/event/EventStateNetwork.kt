package com.example.test.dxworkspace.presentation.utils.event

class EventStateNetwork(val state: StateNetwork) : EventBus.Event()

enum class StateNetwork {
    NONE,
    UN_CONNECT,
    CONNECT,
    UNREACH_HOST,
    SYNCING,
    SYNCING_COMPLETE
}
