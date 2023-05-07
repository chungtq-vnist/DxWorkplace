package com.example.test.dxworkspace.presentation.utils.event

class SPEventToastOrder
    (val textId: Int = 0, val isFail: Boolean = true, val text: String = "") : EventBus.Event()