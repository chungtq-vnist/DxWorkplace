package com.example.test.dxworkspace.data.entity.dashboard_manufacturing

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse


data class DashboardManufacturingCommandModel(
    var _id : String? ="",
    var code : String? = "",
    var manufacturingMill : ManufacturingMillModel?,
    var status : Int? = 1,
    var startDate : String? = ""
){
    var isSelected : Boolean = false
}

data class ManufacturingMillModel(
    var name : String? ="",
)

data class DashboardManufacturingCommandByStatus(
    var command1 : Int? = 0,
    var command2 : Int? = 0,
    var command3 : Int? = 0,
    var command4 : Int? = 0,
    var command5 : Int? = 0,
    var command6 : Int? = 0,
    var listCommand1 : List<DashboardManufacturingCommandModel>? = emptyList(),
    var listCommand2 : List<DashboardManufacturingCommandModel>? = emptyList(),
    var listCommand3 : List<DashboardManufacturingCommandModel>? = emptyList(),
    var listCommand4 : List<DashboardManufacturingCommandModel>? = emptyList(),
    var listCommand5 : List<DashboardManufacturingCommandModel>? = emptyList(),
    var listCommand6 : List<DashboardManufacturingCommandModel>? = emptyList(),
)

data class DashboardManufacturingCommandByProgress(
    var totalCommands : Int? = 0,
    var trueCommands : Int? = 0,
    var slowCommands : Int? = 0,
    var expiredCommands : Int? =0 ,
    var arrayTrueCommands : List<DashboardManufacturingCommandModel>? = emptyList(),
    var arraySlowCommands : List<DashboardManufacturingCommandModel>? = emptyList(),
    var arrayExpiredCommands : List<DashboardManufacturingCommandModel>? = emptyList(),
)



data class DashboardManufacturingCommandByStatusRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : DashboardManufacturingCommandByStatus? = DashboardManufacturingCommandByStatus()
)
data class DashboardManufacturingCommandByProgressRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : DashboardManufacturingCommandByProgress? = DashboardManufacturingCommandByProgress()
)

