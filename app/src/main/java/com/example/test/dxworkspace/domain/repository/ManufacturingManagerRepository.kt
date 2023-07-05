package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.bill.BillByCommandModel
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandEntity
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubSaleOrder
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotEntity
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillEntity
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.manufacturing_work.UserRoleInOrganizationUnit
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.data.entity.product_request.UpdateProductRequest
import com.example.test.dxworkspace.data.entity.role.RoleModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleModel
import com.example.test.dxworkspace.domain.model.manufacturing_plan.RequestManufacturingPlan
import com.example.test.dxworkspace.presentation.model.menu.*

interface ManufacturingManagerRepository {
    suspend fun getALlManufacturingMillsRemote() : Either<Failure,List<ManufacturingMillModel>>

    suspend fun getManufacturingMillById(id : String) : Either<Failure,ManufacturingMillModel>

    suspend fun getAllManufacturingLotsRemote(from : String , to :String) : Either<Failure,List<ManufacturingLotModel>>

    suspend fun getALlAndSaveManufacturingMillsRemote() : Either<Failure,Boolean>

//    suspend fun getAllAndSaveManufacturingLotsRemote() : Either<Failure,Boolean>

    suspend fun getAllManufacturingCommandsRemote() : Either<Failure , Boolean>
    suspend fun getAllManufacturingCommandsRemoteNew(s:String, e: String) : Either<Failure, List<ManufacturingCommandModel>>

    suspend fun getAllMillsFromDb() : List<ManufacturingMillEntity>
    suspend fun getAllLotsFromDb() : List<ManufacturingLotEntity>
    suspend fun getAllCommandsFromDb() : List<ManufacturingCommandEntity>

    suspend fun getAllOrganizationUnitRemote() : Either<Failure, List<OrganizationUnit>>

    suspend fun getAllRolesRemote() : Either<Failure, List<RoleModel>>

    suspend fun getManufacturingWorkById(id : String) : Either<Failure,ManufacturingWorkDetailModel>

    suspend fun getManufacturingCommandById(id : String) : Either<Failure , ManufacturingCommandModel>

    suspend fun getInventoryGoodByIds(ids : List<String>) : Either<Failure , List<InventoryGoodWrap>>

    suspend fun getBillByCommand(id : String) : Either<Failure , List<BillByCommandModel>>

    suspend fun getAllPlan(s:String, e: String) : Either<Failure,List<ManufacturingPlanModel>>

    suspend fun getApprovesOfPlanByRole(id : String) : Either<Failure,List<UserRoleInOrganizationUnit>>

    suspend fun getSaleOrderByRole(id: String) : Either<Failure,List<SubSaleOrder>>

    suspend fun getGoodManageByRole(id : String) : Either<Failure,List<GoodDetailModel>>

    suspend fun getGoodByType(id : String) : Either<Failure,List<GoodDetailModel>>

    suspend fun getFreeUsers(array : List<String> ,role : String) : Either<Failure,List<SubUserBasicModel>>

    suspend fun getWorkScheduleOfMillByDate(mills : List<String>,start : String , end : String) : Either<Failure,List<WorkScheduleModel>>

    suspend fun getAllUser() : Either<Failure , List<UserProfileResponse>>

    suspend fun getStock() : Either<Failure , List<StockModel>>

    suspend fun createExportBill(data : List<BillExportMaterialRequest>) : Either<Failure , Boolean>

    suspend fun updateCommand(data : RequestApproveCommand , id : String) : Either<Failure , Boolean>

    suspend fun updateQualityControlCommand(data : RequestQualityControl, id : String) : Either<Failure , Boolean>

    suspend fun finishCommand(data: RequestFinishCommand ,id:String) :  Either<Failure , Boolean>

    suspend fun createLots(data : List<RequestCreateLot>) :  Either<Failure , Boolean>

    suspend fun getLotById(id : String) : Either<Failure,ManufacturingLotDetailModel>

    suspend fun updateLot(id : String , data : RequestUpdateLot) : Either<Failure,Boolean>

    suspend fun updateInfoLot(id : String , data : RequestUpdateInfoLot) : Either<Failure,Boolean>

    suspend fun getAllProductRequestInManufacturing(requestType : Int , requestFrom : String , from :String,to:String)
        : Either<Failure,List<ProductRequestManagementModel>>

    suspend fun createRequest(data : ParamCreateProductRequest) : Either<Failure,Boolean>

    suspend fun updateRequest(data : UpdateProductRequest, id:String) : Either<Failure,Boolean>

    suspend fun createPlan(data : RequestManufacturingPlan) : Either<Failure,Boolean>

}