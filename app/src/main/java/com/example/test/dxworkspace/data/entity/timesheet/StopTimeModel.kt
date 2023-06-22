package com.example.test.dxworkspace.data.entity.timesheet

data class StopTimeModel(
    val autoStopped : Int? ,
    val description : String? ,
     val employee: String? ,
    val startedAt : String? ,
    val stoppedAt : String? ,
    val timesheetLog : String? ,
    val taskActionStartTimer : String?,  // id lay ra cua lan bam gio nay
    val type : String?
)
