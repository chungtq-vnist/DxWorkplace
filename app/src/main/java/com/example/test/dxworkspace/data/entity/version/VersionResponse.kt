package com.example.test.dxworkspace.data.entity.version

import com.example.test.dxworkspace.data.entity.link.LinkResponse

data class VersionResponse(
    var _id: String = "",
    var role: Int = 0,
    var user: Int = 0,
    var userRole: Int = 0,
    var component: Int = 0,
    var link: Int = 0,
    var privilege: Int = 0,
    var manufacturingWork : Int = 0 ,
)

data class VersionResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : VersionResponse = VersionResponse()
)

data class VersionRequest(
    val ids : List<String> = listOf()
)