package com.example.test.dxworkspace.data.entity.work_schedule

import com.google.gson.annotations.SerializedName

data class ParamWorkSchedule(
    var limit:Int = 10,
    var page:Int = 1,
    @SerializedName("object")
    var objects:String ="",
    var month:String ="",
    var currentRole:String ="",
    var code:String="",
    var user :String = ""
)