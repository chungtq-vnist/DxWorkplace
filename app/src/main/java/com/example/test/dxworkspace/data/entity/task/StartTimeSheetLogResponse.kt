package com.example.test.dxworkspace.data.entity.task

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class StartTimeSheetLogResponse(
    val name : String? = "",
    var timesheetLogs : List<StartTimeSheetLog>? = emptyList(),

    )
data class StartTimeSheetLogResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    var content : StartTimeSheetLogResponse? = StartTimeSheetLogResponse(),

    )

data class StartTimeSheetLog(
    var autoStopped: Int? = 0,  // 1: Tắt bấm giờ bằng tay, 2: Tắt bấm giờ tự động với thời gian hẹn trc, 3: add log timer
    var acceptLog: Boolean? = false,
    var startedAt :String? ="",
    var _id: String = "",

)
