package com.example.test.dxworkspace.data.entity.manufacturing_work

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class ManufacturingWorkModel(
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
    var manageRoles : List<String>? = listOf(),
    var organizationalUnit : OrganizationUnit? = null,
)

data class ManufacturingMill(
    var _id : String ="",
    var code : String = "",
    var description : String? ="" ,
    var name : String ="",
)

data class OrganizationUnit(
    var _id : String ="",
    var createdAt : String ="",
    var udpatedAt : String ="",
    var name : String ="",
    var parent : String? = "",
    var managers : List<RoleInOrganizationUnit>? = listOf(),
    var employees : List<RoleInOrganizationUnit>? = listOf(),
    var deputyManagers : List<RoleInOrganizationUnit>? = listOf(),
    )

data class RoleInOrganizationUnit(
    var _id : String ="",
    var name : String = "",
    var parents : List<String>? = listOf(),
    var users : List<UserRoleInOrganizationUnit>? = listOf(),
)

data class UserRoleInOrganizationUnit(
    var roleId : String? = "",
    var userId : UserProfileResponse? = null,
)