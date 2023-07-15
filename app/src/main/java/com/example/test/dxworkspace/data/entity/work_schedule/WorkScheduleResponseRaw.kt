package com.example.test.dxworkspace.data.entity.work_schedule

import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingMill

data class WorkScheduleResponseRaw (
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: WorkScheduleResponseWrap? = null
        )

data class WorkScheduleResponseWrap(
    val workSchedules :WorkScheduleResponse? = WorkScheduleResponse()
)
data class WorkScheduleResponse(
    val docs : List<WorkScheduleDetailModel>? = listOf()
)

data class WorkScheduleDetailModel(
    val _id: String? = "",
    val manufacturingMill: ManufacturingMill? = null,
    val user : SubUserBasicModel? = null,
    val month: String = "",
    val turns: MutableList<MutableList<SubCommandInWorkSchedule?>> = mutableListOf()
)