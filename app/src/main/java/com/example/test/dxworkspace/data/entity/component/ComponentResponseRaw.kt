package com.example.test.dxworkspace.data.entity.component

import com.example.test.dxworkspace.data.entity.login.LoginResponseContent
import com.google.gson.annotations.SerializedName

data class ComponentResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : MutableList<ComponentResponse> = mutableListOf()
)

data class ComponentResponse(
    var _id : String ="",
    var name : String ="",
    var description : String ="",
    var deleteSoft : Boolean = false,
)