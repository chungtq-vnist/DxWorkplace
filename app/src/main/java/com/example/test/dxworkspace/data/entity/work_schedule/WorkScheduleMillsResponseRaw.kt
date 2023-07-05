package com.example.test.dxworkspace.data.entity.work_schedule

data class WorkScheduleMillsResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: WorkScheduleMillsResponseWrap? = null
)

data class WorkScheduleMillsResponseWrap(
    val workSchedules: List<WorkScheduleModel>? = listOf()
)

data class WorkScheduleModel(
    val _id: String? = "",
    val manufacturingMill: String = "",
    val month: String = "",
    val turns: MutableList<MutableList<SubCommandInWorkSchedule?>> = mutableListOf()
)

data class SubCommandInWorkSchedule(
    val _id: String = "",
    val status: Int = 0,
    val code: String = "",

    ){
    var isSave = false
}