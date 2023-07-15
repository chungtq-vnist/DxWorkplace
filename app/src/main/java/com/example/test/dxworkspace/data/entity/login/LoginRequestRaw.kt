package com.example.test.dxworkspace.data.entity.login

data class LoginRequestRaw(
    val email : String ="",
    val password : String ="",
    val portal : String ="",
    var pushNotificationToken :String =""
)
