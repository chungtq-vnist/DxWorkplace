package com.example.test.dxworkspace.data.entity.manufacturing_work

import com.example.test.dxworkspace.data.entity.role.RoleModel

data class ManufacturingWorkDetailResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: ManufacturingWorkDetailResponse? = ManufacturingWorkDetailResponse()
)

data class ManufacturingWorkDetailResponse(
    val manufacturingWorks : ManufacturingWorkDetailModel? = ManufacturingWorkDetailModel()
)

data class ManufacturingWorkDetailModel(
    var _id : String ="",
    var address : String ="",
    var code : String = "",
    var createdAt : String ="",
    var description : String? ="",
    var name : String ="",
    var phoneNumber : String ="",
    var status : Int = 1,
    var udpatedAt : String ="",
    var manufacturingMills : List<ManufacturingMill>? = listOf(),
    var manageRoles : List<RoleModel>? = listOf(),
    var organizationalUnit : OrganizationUnit? = null,
    var turn : Int? = 0
)