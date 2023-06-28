package com.example.test.dxworkspace.data.entity.role

import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkResponse

data class RoleResponseRawWrap (
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: List<RoleModel> = listOf()
        )

data class RoleModel(
    var _id : String = "" ,
    var name : String? ="",
    var createdAt : String = ""
)
