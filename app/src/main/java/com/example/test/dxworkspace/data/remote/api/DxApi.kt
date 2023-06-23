package com.example.test.dxworkspace.data.remote.api

import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.*
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkResponseRaw
import com.example.test.dxworkspace.data.entity.task.TaskDetailResponseRaw
import com.example.test.dxworkspace.data.entity.task.TaskResponseRaw
import com.example.test.dxworkspace.data.entity.task.TimeSheetResponseRaw
import com.example.test.dxworkspace.data.entity.timesheet.StartTimeModel
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.data.entity.version.VersionDiffResponseRaw
import com.example.test.dxworkspace.data.entity.version.VersionRequest
import com.example.test.dxworkspace.data.entity.version.VersionResponseRaw
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

// tổng hợp tất cả các API dùng trong app
interface DxApi : LoginApi {

    // notification
    @GET("/notifications/get-notifications")
    fun getALlNotifications() : Unit

    // get manual notification
    @GET("/notifications/get")
    fun getAllManualNotification() : Unit


    // task and kpi

    @GET("/task/tasks")
    fun getTasks(
        @Query("type") type : String ="",
        @Query("user") user : String ,
        @Query("perPage") perPage : Int = 1000,
        @Query("startDate") startDate : String ,
        @Query("endDate")  endDate : String ,
        @Query("aPeriodOfTime") aPeriodOfTime : Boolean = true,
    ) : Call<TaskResponseRaw>

    @GET("/performtask/tasks/{id}")
    fun getTaskById(
        @Path("id") id : String
    ) : Call<TaskDetailResponseRaw>

    // version
    @GET("/version/versions")
    fun getAllVersions() : Call<VersionResponseRaw>

    @GET("/manufacturing-works/version")
    fun getListIdFromVersion(
        @Query("version") version :Int
    ) : Call<VersionDiffResponseRaw>



    //manufacturing

    @GET("/manufacturing-works")
    fun getAllManufacturingWorks(
        @Query("currentRole") role : String
    ) : Call<ManufacturingWorkResponseRaw>
    @GET("/manufacturing-works/version/ids")
    fun getManufacturingWorkWithIds(
        @Query("currentRole") role : String,
        @Body ids : VersionRequest
    ) : Call<ManufacturingWorkResponseRaw>


    @POST("/performtask/tasks/{id}/timesheet-logs/start-timer")
    fun startTimer(
        @Path("id") id : String,
        @Body start : StartTimeModel
    ) : Call<TaskDetailResponseRaw>

    @POST("performtask/tasks/{id}/timesheet-logs/stop-timer")
    fun stopTimer(
        @Path("id") id : String,
        @Body start : StopTimeModel
    ) : Call<TaskDetailResponseRaw>

    @POST("/performtask/tasks/{id}/task-actions")
    fun postAction(
        @Path("id") id : String,
        @Body body : RequestBody
    ) : Call<TimeSheetResponseRaw>

    // manufacturing dashboard
    @GET("/manufacturing-dashboard/get-number-plans-by-status")
    fun getNumberOfPlanByStatus(
        @Query("currentRole") curRole : String ,
        @Query("manufacturingWorks") work : List<String>?,
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
    ) : Call<DashboardManufacturingPlanByStatusRaw>

    @GET("/manufacturing-dashboard/get-number-plans-by-progress")
    fun getNumberOfPlanByProgress(
        @Query("currentRole") curRole : String ,
        @Query("manufacturingWorks") work : List<String>?,
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
    ) : Call<DashboardManufacturingPlanByProgressRaw>


    @GET("/manufacturing-dashboard/get-number-commands-by-status")
    fun getNumberOfCommandByStatus(
        @Query("currentRole") curRole : String ,
        @Query("manufacturingWorks") work : List<String>?,
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
    ) : Call<DashboardManufacturingCommandByStatusRaw>

    @GET("/manufacturing-dashboard/get-number-commands-by-progress")
    fun getNumberOfCommandByProgress(
        @Query("currentRole") curRole : String ,
        @Query("manufacturingWorks") work : List<String>?,
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
    ) : Call<DashboardManufacturingCommandByProgressRaw>



    @GET("/manufacturing-dashboard/get-number-request-by-status")
    fun getNumberOfRequestByStatus(
        @Query("currentRole") curRole : String ,
        @Query("manufacturingWorks") work : List<String>?,
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
    ) : Call<DashboardManufacturingRequestByStatusRaw>

    @GET("/manufacturing-dashboard/get-number-request-by-type")
    fun getNumberOfRequestByType(
        @Query("currentRole") curRole : String ,
        @Query("manufacturingWorks") work : List<String>?,
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
    ) : Call<DashboardManufacturingRequestByTypeRaw>




}