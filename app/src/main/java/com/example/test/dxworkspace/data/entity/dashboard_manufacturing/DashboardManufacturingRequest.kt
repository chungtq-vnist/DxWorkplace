package com.example.test.dxworkspace.data.entity.dashboard_manufacturing

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class DashboardManufacturingRequestModel(
    var _id : String? ="",
    var code : String? = "",
    var status : Int? = 1,
    var creator : UserProfileResponse,
    var createdAt : String? = "",
    var type : Int? = 1,

    ){
    var isSelected : Boolean = false
}

data class DashboardManufacturingRequestByStatus(
    var request1 : Int? = 0,
    var request2 : Int? = 0,
    var request3 : Int? = 0,
    var request4 : Int? = 0,
    var request5 : Int? = 0,
    var request6 : Int? = 0,
    var listStatus11 : List<DashboardManufacturingRequestModel>? = emptyList(),
    var listStatus21 : List<DashboardManufacturingRequestModel>? = emptyList(),
    var listStatus31 : List<DashboardManufacturingRequestModel>? = emptyList(),
    var listStatus41 : List<DashboardManufacturingRequestModel>? = emptyList(),
    var listStatus51 : List<DashboardManufacturingRequestModel>? = emptyList(),
    var listStatus61 : List<DashboardManufacturingRequestModel>? = emptyList(),
)

data class DashboardManufacturingRequestByType(
    var request1 : Int? = 0,
    var request2 : Int? = 0,
    var request3 : Int? = 0,
    var listRequestPurchase : List<DashboardManufacturingRequestModel>? = emptyList(),
    var listRequestImport : List<DashboardManufacturingRequestModel>? = emptyList(),
    var listRequestExport : List<DashboardManufacturingRequestModel>? = emptyList(),
)



data class DashboardManufacturingRequestByStatusRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : DashboardManufacturingRequestByStatus? = DashboardManufacturingRequestByStatus()
)
data class DashboardManufacturingRequestByTypeRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : DashboardManufacturingRequestByType? = DashboardManufacturingRequestByType()
)