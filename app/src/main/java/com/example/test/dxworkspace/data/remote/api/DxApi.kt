package com.example.test.dxworkspace.data.remote.api

import com.example.test.dxworkspace.data.entity.bill.BillByCommandResponseRaw
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.*
import com.example.test.dxworkspace.data.entity.good.GoodManageByRoleResponseRaw
import com.example.test.dxworkspace.data.entity.good.GoodResponseRaw
import com.example.test.dxworkspace.data.entity.good.InventoryGoodResponseRaw
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandDetailRaw
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandResponseRaw
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockResponseRaw
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotDetailResponseRaw
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotResponseRaw
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillByIdResponseRaw
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillResponseRaw
import com.example.test.dxworkspace.data.entity.manufacturing_plan.*
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkDetailResponseRaw
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkResponseRaw
import com.example.test.dxworkspace.data.entity.organization_unit.OrganizationUnitResponseRaw
import com.example.test.dxworkspace.data.entity.product_request.*
import com.example.test.dxworkspace.data.entity.project.ProjectResponseRaw
import com.example.test.dxworkspace.data.entity.report.FinancialReportResponseRaw
import com.example.test.dxworkspace.data.entity.report.PlanCompletedOnScheduleRaw
import com.example.test.dxworkspace.data.entity.report.SaleReportResponseRaw
import com.example.test.dxworkspace.data.entity.report.WarehouseReportResponseRaw
import com.example.test.dxworkspace.data.entity.role.RoleResponseRawWrap
import com.example.test.dxworkspace.data.entity.task.*
import com.example.test.dxworkspace.data.entity.timesheet.StartTimeModel
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.data.entity.user.AllUsersResponseRaw
import com.example.test.dxworkspace.data.entity.version.VersionDiffResponseRaw
import com.example.test.dxworkspace.data.entity.version.VersionRequest
import com.example.test.dxworkspace.data.entity.version.VersionResponseRaw
import com.example.test.dxworkspace.data.entity.work_schedule.UserFreeWorkScheduleResponseRaw
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleMillsResponseRaw
import com.example.test.dxworkspace.domain.model.manufacturing_plan.RequestManufacturingPlan
import com.example.test.dxworkspace.presentation.model.menu.*
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



    @POST("/performtask/tasks/{id}/timesheet-logs/start-timer")
    fun startTimer(
        @Path("id") id : String,
        @Body start : StartTimeModel
    ) : Call<StartTimeSheetLogResponseRaw>

    @POST("performtask/tasks/{id}/timesheet-logs/stop-timer")
    fun stopTimer(
        @Path("id") id : String,
        @Body start : StopTimeModel
    ) : Call<TaskDetailResponseRaw>

    @POST("/performtask/tasks/{id}/task-actions")
    fun postAction(
        @Path("id") id : String,
        @Body body : RequestBody
    ) : Call<CreateTaskActionResponseRaw>

    @POST("/performtask/tasks/{id}")
    fun requestCloseTask(
        @Path("id") id : String,
        @Body body : RequestCloseTask
    ) : Call<TaskDetailResponseRaw>

    @GET("/task/task-templates")
    fun getTemplatesByUserId(
        @Query("userId")userId : String
    ) : Call<TaskTemplateResponseRaw>

    @GET("/projects/project")
    fun getAllProject(

    ) : Call<ProjectResponseRaw>


//    @FormUrlEncoded
//    @POST("/task/tasks")
//    fun createTask(
//        @Body body : RequestBody
//    ) : Call<TaskDetailResponseRaw>

    @FormUrlEncoded
    @POST("/task/tasks")
    fun createTask(
        @FieldMap body : Map<String,String>
    ) : Call<TaskDetailResponseRaw>




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


    @GET("/manufacturing-dashboard/get-goods-manufacturing-report")
    fun getReportGoodsQuality(
        @Query("currentRole") curRole : String ,
        @Query("manufacturingWorks") work : List<String>?,
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
        @Query("fromDateCompare") fromDateCompare : String ? ,
        @Query("toDateCompare") toDateCompare : String? ,
    ) : Call<DashboardManufacturingQualityGoodsRaw>


    @GET("/report/financial-report")
    fun getFinancialReport(
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
        @Query("fromDateCompare") fromDateCompare : String ? ,
        @Query("toDateCompare") toDateCompare : String?
    ) : Call<FinancialReportResponseRaw>


    @GET("/sale-report")
    fun getSaleReport(
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
        @Query("fromDateCompare") fromDateCompare : String ? ,
        @Query("toDateCompare") toDateCompare : String?
    ) : Call<SaleReportResponseRaw>

    @GET("/manufacturing-dashboard/get-number-plan-completed-on-schedule")
    fun getNumberPlanCompletedOnSchedule(
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
        @Query("fromDateCompare") fromDateCompare : String ? ,
        @Query("toDateCompare") toDateCompare : String?
    ) : Call<PlanCompletedOnScheduleRaw>

    @GET("/manufacturing-works/version")
    fun getListIdFromVersion(
        @Query("version") version :Int
    ) : Call<VersionDiffResponseRaw>

    //manufacturing

    @GET("/manufacturing-works")
    fun getAllManufacturingWorks(
        @Query("currentRole") role : String?
    ) : Call<ManufacturingWorkResponseRaw>
    @GET("/manufacturing-works/version/ids")
    fun getManufacturingWorkWithIds(
        @Query("currentRole") role : String,
        @Body ids : VersionRequest
    ) : Call<ManufacturingWorkResponseRaw>


    @GET("/manufacturing-mill")
    fun getAllManufacturingMills(
        @Query("page") page : Int = 1,
        @Query("limit") limit : Int = 100,
        @Query("currentRole") role : String
//        -- hiện tại việc get xưởng đang ko cần check gì cả / hoặc là check theo vai trò quản lý
    // nhà máy
    ) : Call<ManufacturingMillResponseRaw>

    @GET("/manufacturing-mill/{id}")
    fun getManufacturingMillById(
        @Path("id") id : String
    ) : Call<ManufacturingMillByIdResponseRaw>

    @GET("/lot/get-manufacturing-lot")
    fun getAllManufacturingLots(
        @Query("page") page : Int =1 ,
        @Query("limit") limit : Int = 1000,
        @Query("currentRole") role : String,
        @Query("from") from : String ,
        @Query("to") to : String
    ) : Call<ManufacturingLotResponseRaw>

    @GET("/lot/get-manufacturing-lot/{id}")
    fun getLotById(
        @Path("id") id : String
    ) : Call<ManufacturingLotDetailResponseRaw>



    @GET("/manufacturing-command")
    fun getAllManufacturingCommand(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 1000,
        @Query("currentRole") role: String ,
        @Query("endDate") endDate : String?,
        @Query("startDate") startDate : String?,
    ) : Call<ManufacturingCommandResponseRaw>

    @GET("/manufacturing-command/{id}")
    fun getManufacturingCommandById(
        @Path("id") id : String
    ) : Call<ManufacturingCommandDetailRaw>

    @GET("/lot/get-inventory")
    fun getInventoryGood(
        @Query("array") array : List<String>?
    ) : Call<InventoryGoodResponseRaw>

    // get bill de nghi xuat nguyen vat lieu cua lenh san xuat
    @GET("/bills/bill-by-command")
    fun getBillOfCommand(
        @Query("manufacturingCommandId") id : String
    ) : Call<BillByCommandResponseRaw>

    @GET("/manufacturing-plan")
    fun getAllPlan(
        @Query("to") endDate : String?,
        @Query("from") startDate : String?,
        @Query("currentRole") role: String ,
        @Query("limit") limit :Int = 1000,
        @Query("page") page :Int = 1
    ) : Call<ManufacturingPlanResponseRaw>

    @GET("/manufacturing-plan/get-approvers-of-plan/{id}")
    fun getApprovesOfPlanByRole(
        @Path("id") id : String
    ) : Call<ApproversManufacturingPlanResponseRaw>


    @GET("/sales-order/get-by-manufacturing-works/{id}")
    fun getSaleOrderByRole(
        @Path("id") id : String
    ) : Call<SalesOrderResponseRaw>

    @GET("/goods/by-manage-works-role/role/{id}")
    fun getGoodManageByRole(
        @Path("id") id : String
    ) : Call<GoodManageByRoleResponseRaw>

    @GET("/goods")
    fun getAllGoods(
        @Query("type") type :String
    ) :Call<GoodResponseRaw>

    @GET("/work-schedule/manufacturingMills")
    fun getWorkScheduleOfMillByDate(
        @Query("manufacturingMills") mills : List<String>,
        @Query("startDate") start : String ,
        @Query("endDate") end : String,
    ) : Call<WorkScheduleMillsResponseRaw>

    @GET("/work-schedule/worker/array-schedules")
    fun getFreeUsers(
        @Query("arrayWorkerSchedules[]" ,encoded = false) array : List<String>,
        @Query("currentRole") role : String,
    ) : Call<UserFreeWorkScheduleResponseRaw>

    @POST("/manufacturing-plan")
    fun createPlan(
        @Body plan : RequestManufacturingPlan
    ) : Call<CreatePlanResponseRaw>

    @GET("/stocks")
    fun getStocks() : Call<StockResponseRaw>

    @POST("/bills/create-many-product-bills")
    fun createExportBills(
        @Body data : List<BillExportMaterialRequest>
    ) : Call<Unit>

    @PATCH("/manufacturing-command/{id}")
    fun updateCommand(
        @Path("id") id : String ,
        @Body data : RequestApproveCommand
    ) : Call<Unit>


    @PATCH("/manufacturing-command/{id}")
    fun updateQualityControlCommand(
        @Path("id") id : String ,
        @Body data : RequestQualityControl
    ) : Call<Unit>

    @PATCH("/manufacturing-command/{id}")
    fun finishCommand(
        @Path("id") id : String ,
        @Body data : RequestFinishCommand
    ) : Call<Unit>

    @POST("/lot/create-manufacturing-lot")
    fun createLots(
        @Body listLot : List<RequestCreateLot>
    ) : Call<Unit>

    @GET("/product-request-management")
    fun getAllProductRequestInManufacturing(
        @Query("requestType") requestType : Int ,
        @Query("requestFrom") requestFrom :String? ,
        @Query("from") from :String? ,
        @Query("to") to :String?
    ) : Call<ProductRequestManagementResponseRaw>

    @GET("/product-request-management/ids")
    fun getListRequestByIds(
        @Query("ids") ids : List<String>
    ) : Call<ListProductRequestResponseRaw>

    @POST("/product-request-management")
    fun createRequest(
        @Body data : ParamCreateProductRequest
    ) : Call<Unit>

    @PATCH("/product-request-management/{id}")
    fun updateRequest(
        @Path("id") id : String,
        @Body data : ParamCreateProductRequest
    ) : Call<Unit>


    @PATCH("/product-request-management/{id}")
    fun updateStatusRequest(
        @Path("id") id : String,
        @Body data : ParamUpdateRequest
    ) : Call<Unit>

    @POST("/product-request-management/create-many-request")
    fun createManyRequest(
        @Body data : List<ParamCreateProductRequest>
    ) : Call<Unit>

    @GET("/manufacturing-works/{id}")
    fun getManufacturingWorkById(
        @Path("id") id : String,
    ) : Call<ManufacturingWorkDetailResponseRaw>


    @PATCH("/lot/{id}")
    fun updateLot(
        @Path("id") di : String,
        @Body data : RequestUpdateLot
    ) : Call<Unit>

    @PATCH("/lot/{id}")
    fun updateInfoLot(
        @Path("id") di : String,
        @Body data : RequestUpdateInfoLot
    ) : Call<Unit>

    @GET("/role/roles")
    fun getAllRoles(
    ) : Call<RoleResponseRawWrap>

    @GET("/organizational-units/organizational-units")
    fun getALlOrganizationUnits(

    ) : Call <OrganizationUnitResponseRaw>

    @GET("/user/users")
    fun getAllUser(
    ) : Call<AllUsersResponseRaw>

    @GET("/warehouse-report/warehouse-report")
    fun getWarehouseReport(
        @Query("fromDate") fromDate : String? ,
        @Query("toDate") toDate : String? ,
        @Query("fromDateCompare") fromDateCompare : String ? ,
        @Query("toDateCompare") toDateCompare : String?
    ) : Call<WarehouseReportResponseRaw>
}