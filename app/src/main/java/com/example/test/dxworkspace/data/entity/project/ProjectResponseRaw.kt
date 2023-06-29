package com.example.test.dxworkspace.data.entity.project

data class ProjectResponseRaw (
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : List<ProjectResponse>? = listOf()
        )

data class ProjectResponse(
    val projectType : Int = 0 ,
    val _id : String="",
    val name : String =""
)
// chỉ áp dụng các project có type = 1 khi tạo task mới