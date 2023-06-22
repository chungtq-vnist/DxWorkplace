package com.example.test.dxworkspace.data.entity.manufacturing_work

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class ManufacturingWorkResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: ManufacturingWorkResponse = ManufacturingWorkResponse()
)

data class ManufacturingWorkResponse(
    val allManufacturingWorks : ManufacturingWorkResponse2 = ManufacturingWorkResponse2()
)


data class ManufacturingWorkResponse2(
    val docs : List<ManufacturingWorkModel>? = listOf()
)