package com.example.test.dxworkspace.data.entity.department

data class UserInDepartmentResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),

)

data class DepartmentModel(
    var department : String? = "",
    var id : String ="",

)