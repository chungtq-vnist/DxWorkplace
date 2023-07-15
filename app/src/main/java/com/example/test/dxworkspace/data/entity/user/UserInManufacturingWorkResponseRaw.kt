package com.example.test.dxworkspace.data.entity.user

import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
data class UserInManufacturingWorkResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: UserInManufacturingWorkResponse? = null
)
data class UserInManufacturingWorkResponse(
    val employees : List<UserInManufacturingWork>? = listOf()
)

data class UserInManufacturingWork(
    val userId: SubUserBasicModel = SubUserBasicModel()
)