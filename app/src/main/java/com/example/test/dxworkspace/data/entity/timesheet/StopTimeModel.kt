package com.example.test.dxworkspace.data.entity.timesheet

data class StopTimeModel(
    var autoStopped : Int? ,
    var description : String? ,
    var employee: String? ,
    var startedAt : String? ,
    var stoppedAt : String? ,
    var timesheetLog : String? ,
    var taskActionStartTimer : String?,  // id lay ra cua lan bam gio nay
    var type : String?
)
