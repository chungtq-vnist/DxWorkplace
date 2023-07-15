package com.example.test.dxworkspace.data.entity.timesheet

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class TimeSheetLogResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : TimeSheetLogResponse? = null
)

data class TimeSheetLogResponse(
    val _id:String="", // id task dang duoc bam gio
    val name:String="",
    val timesheetLogs : List<SubTimeSheetLog>? = listOf()
)

data class SubTimeSheetLog(
    var autoStopped: Int? = 0,  // 1: Tắt bấm giờ bằng tay, 2: Tắt bấm giờ tự động với thời gian hẹn trc, 3: add log timer
    var acceptLog: Boolean? = false,
    var _id: String = "",
    var startedAt: String = "",
    var creator: String? = "",
    var description: String? = "",
)