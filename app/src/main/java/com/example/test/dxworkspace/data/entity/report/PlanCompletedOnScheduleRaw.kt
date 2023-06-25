package com.example.test.dxworkspace.data.entity.report

data class PlanCompletedOnScheduleRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : PlanCompletedOnScheduleContent? = PlanCompletedOnScheduleContent()
)


data class PlanCompletedOnScheduleModel(
    val numberPlanCompleted : Int? = 0,
    val numberPlanNeedCompleted : Int? = 0
)
data class PlanCompletedOnScheduleContent(
    val data : List<PlanCompletedOnScheduleModel>? = listOf(),
    val dataCompare : List<PlanCompletedOnScheduleModel>? = listOf(),
)
