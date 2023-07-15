package com.example.test.dxworkspace.data.entity.work_schedule

data class ParamCreateWorkSchedule(
    var allManufacturingMill: Boolean?,
    var allUser :Boolean? ,
    var month: String,
    var numberOfTurns: Int,
    var currentRole: String,
    var manufacturingMill:String? ,
    var user:String?
)
