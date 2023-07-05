package com.example.test.dxworkspace.presentation.model.menu

data class RequestFinishCommand (
    val finishedProductQuantity: String ="",
    val substandardProductQuantity: String ="",
    val finishedTime: String = "",
    val status: Int = 0
        )