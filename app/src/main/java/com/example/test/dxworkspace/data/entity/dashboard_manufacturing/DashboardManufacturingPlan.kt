package com.example.test.dxworkspace.data.entity.dashboard_manufacturing

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class DashboardManufacturingPlanModel(
    var _id : String? ="" ,
    var code : String? = "",
    var creator : UserProfileResponse,
    var status : Int? = 1,
    var startDate : String? = ""
){
    var isSelected : Boolean = false
}

data class DashboardManufacturingPlanByStatus(
    var plan1 : Int? = 0,
    var plan2 : Int? = 0,
    var plan3 : Int? = 0,
    var plan4 : Int? = 0,
    var plan5 : Int? = 0,
    var listPlan1 : List<DashboardManufacturingPlanModel>? = emptyList(),
    var listPlan2 : List<DashboardManufacturingPlanModel>? = emptyList(),
    var listPlan3 : List<DashboardManufacturingPlanModel>? = emptyList(),
    var listPlan4 : List<DashboardManufacturingPlanModel>? = emptyList(),
    var listPlan5 : List<DashboardManufacturingPlanModel>? = emptyList(),
) {
    override fun equals(other: Any?): Boolean {
        if(other is DashboardManufacturingPlanByStatus){
            other as DashboardManufacturingPlanByStatus
            return other.plan1 == plan1 && other.plan2 == plan2 && other.plan3 == plan3
                    && other.plan4 == plan4 && other.plan5 == plan5
        }else return false
    }
}

data class DashboardManufacturingPlanByProgress(
    var totalPlans : Int? = 0,
    var truePlans : Int? = 0,
    var slowPlans : Int? = 0,
    var expiredPlans : Int? = 0,
    var listSlowPlans : List<DashboardManufacturingPlanModel>? = emptyList(),
    var listExpiredPlans : List<DashboardManufacturingPlanModel>? = emptyList(),
    var listTruePlans : List<DashboardManufacturingPlanModel>? = emptyList(),
) {
    override fun equals(other: Any?): Boolean {
        if(other is DashboardManufacturingPlanByProgress){
            other as DashboardManufacturingPlanByProgress
            return other.totalPlans == totalPlans && other.truePlans == truePlans && other.slowPlans == slowPlans
                    && other.expiredPlans == expiredPlans
        }else return false
    }
}



data class DashboardManufacturingPlanByStatusRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : DashboardManufacturingPlanByStatus? = DashboardManufacturingPlanByStatus()
)
data class DashboardManufacturingPlanByProgressRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : DashboardManufacturingPlanByProgress? = DashboardManufacturingPlanByProgress()
)


data class ModelRequestDashboardPlan(
    var role : String ,
    var works : List<String>? ,
    var fromDate : String? ,
    var toDate : String?
)

data class ModelRequestDashboardGoodQuality(
    var role : String ,
    var works : List<String>? ,
    var fromDate : String? ,
    var toDate : String?,
    var fromDateCompare : String? ,
    var toDateCompare : String?
)
