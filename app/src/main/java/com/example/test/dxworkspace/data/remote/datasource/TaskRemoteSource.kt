package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.entity.component.ComponentResponseRaw
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.data.entity.task.RequestCloseTask
import com.example.test.dxworkspace.data.entity.timesheet.StartTimeModel
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.data.entity.version.VersionResponseRaw
import com.example.test.dxworkspace.data.remote.api.DxApi
import okhttp3.RequestBody
import retrofit2.Call
import javax.inject.Inject

class TaskRemoteSource @Inject constructor(val api: DxApi) {

    fun getTask(
        type: String, user: String, perPage: Int, startDate: String,
        endDate: String, aPeriodOfTime: Boolean
    ) = api.getTasks(type, user, perPage, startDate, endDate, aPeriodOfTime)

    fun getTaskById(
        id : String
    ) = api.getTaskById(id)

    fun startTimer(id : String , p : StartTimeModel) = api.startTimer(id,p)

    fun stopTimer(id : String , p : StopTimeModel) = api.stopTimer(id,p)

    fun postAction(id : String , body : RequestBody) = api.postAction(id, body)

    fun requestToCloseTask(id : String , body : RequestCloseTask) = api.requestCloseTask(id,body)

    fun createTask(body : Map<String,String>) = api.createTask(body)

    fun getAllTemplate(userId : String) = api.getTemplatesByUserId(userId)

    fun getAllProject() = api.getAllProject()

}