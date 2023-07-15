package com.example.test.dxworkspace.presentation.model

import com.google.gson.annotations.SerializedName

data class NotificationFirebaseModel(
    @SerializedName("gcm.notification.title")
    val title:String? = "",
    @SerializedName("gcm.notification.body")
    val body:String? ="",
    val level: String? ="",
    val content: String? ="",
    val sender: String? ="",
    val type :String? =""
)
