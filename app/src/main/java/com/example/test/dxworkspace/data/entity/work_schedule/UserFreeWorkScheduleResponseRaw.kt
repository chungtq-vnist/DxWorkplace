package com.example.test.dxworkspace.data.entity.work_schedule

import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel

data class UserFreeWorkScheduleResponseRaw (
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: UserFreeWorkScheduleResponseWrap? = null
        )

data class UserFreeWorkScheduleResponseWrap(
    val workers : List<SubUserBasicModel>? = listOf()
)