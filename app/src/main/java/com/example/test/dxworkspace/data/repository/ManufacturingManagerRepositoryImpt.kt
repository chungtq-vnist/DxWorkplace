package com.example.test.dxworkspace.data.repository

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
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ParamUpdatePlan
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.manufacturing_work.UserRoleInOrganizationUnit
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.data.entity.product_request.ParamUpdateRequest
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.data.entity.product_request.UpdateProductRequest
import com.example.test.dxworkspace.data.entity.role.RoleModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleModel
import com.example.test.dxworkspace.data.local.datasource.ManufacturingCommandLocalSource
import com.example.test.dxworkspace.data.local.datasource.ManufacturingLotLocalSource
import com.example.test.dxworkspace.data.local.datasource.ManufacturingMillLocalSource
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.LoginRemoteSource
import com.example.test.dxworkspace.data.remote.datasource.ManufacturingManagerRemoteSource
import com.example.test.dxworkspace.domain.model.manufacturing_plan.RequestManufacturingPlan
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingManagerRepository
import com.example.test.dxworkspace.presentation.model.menu.*
import com.google.gson.Gson
import javax.inject.Inject

class ManufacturingManagerRepositoryImpt @Inject constructor(
    val manufacturingManagerRemoteSource: ManufacturingManagerRemoteSource,
    val configRepository: ConfigRepository,
    val gson: Gson,
    val manufacturingCommandLocalSource: ManufacturingCommandLocalSource,
    val manufacturingMillLocalSource: ManufacturingMillLocalSource,
    val manufacturingLotLocalSource: ManufacturingLotLocalSource,
    val loginRemoteSource: LoginRemoteSource
) : ManufacturingManagerRepository {
    override suspend fun getALlManufacturingMillsRemote(): Either<Failure, List<ManufacturingMillModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getAllManufacturingMills(configRepository.getCurrentRole().id),
            {
                if (it.success) it.content?.manufacturingMills?.docs ?: listOf() else listOf()
            },
            listOf()
        )
    }

    override suspend fun getManufacturingMillById(id: String): Either<Failure, ManufacturingMillModel> {
        return requestApi(
            manufacturingManagerRemoteSource.getManufacturingMillById(id),
            {
                if (it.success) it.content?.manufacturingMill
                    ?: ManufacturingMillModel() else ManufacturingMillModel()
            },
            ManufacturingMillModel()
        )
    }

    override suspend fun getALlAndSaveManufacturingMillsRemote(): Either<Failure, Boolean> {
        val r = requestApi(
            manufacturingManagerRemoteSource.getAllManufacturingMills(configRepository.getCurrentRole().id),
            {
                if (it.success) it.content?.manufacturingMills?.docs ?: listOf() else listOf()
            },
            listOf()
        )
        if (r.isRight) {
            val t = r.getValue()
            val q = t.map {
                ManufacturingMillEntity().apply {
                    _id = it._id
                    code = it.code
                    name = it.name
                    description = it.description
                    createdAt = it.createdAt
                    status = it.status
                    manufacturingWorks = gson.toJson(it.manufacturingWorks)
                    teamLeader = gson.toJson(it.teamLeader)
                }
            }
            manufacturingMillLocalSource.savesAndDelete(q, configRepository.getDBName())
            return Either.Right(true)
        } else return Either.Left(Failure.ResponseError())
    }

    override suspend fun getAllManufacturingLotsRemote(from: String, to :String): Either<Failure, List<ManufacturingLotModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getAllManufacturingLots(configRepository.getCurrentRole().id,from,to),
            {
                if (it.success) it.content?.lots?.docs ?: listOf() else listOf()
            },
            listOf()
        )
    }

//    override suspend fun getAllAndSaveManufacturingLotsRemote(): Either<Failure, Boolean> {
//        val r = requestApi(
//            manufacturingManagerRemoteSource.getAllManufacturingLots(configRepository.getCurrentRole().id),
//            {
//                if (it.success) it.content?.lots?.docs ?: listOf() else listOf()
//            },
//            listOf()
//        )
//        if (r.isRight) {
//            val t = r.getValue()
//            val q = t.map {
//                ManufacturingLotEntity().apply {
//                    _id = it._id
//                    code = it.code
//                    type = it.type
//                    description = it.description
//                    createdAt = it.createdAt
//                    productType = it.productType
//                    status = it.status
//                    expirationDate = it.expirationDate
//                    creator = gson.toJson(it.creator)
//                    bills = it.bills?.joinToString(",") ?: ""
//                    passedQualityControl = it.passedQualityControl
//                    originalQuantity = it.originalQuantity
//                    quantity = it.quantity
//                    good = gson.toJson(it.good)
//                }
//            }
//            manufacturingLotLocalSource.savesAndDelete(q, configRepository.getDBName())
//            return Either.Right(true)
//        } else return Either.Left(Failure.ResponseError())
//    }

    override suspend fun getAllManufacturingCommandsRemoteNew(s : String , e : String): Either<Failure, List<ManufacturingCommandModel>> =
        requestApi(
            manufacturingManagerRemoteSource.getAllManufacturingCommand(configRepository.getCurrentRole().id,s,e),
            {
                if (it.success) it.content?.manufacturingCommands?.docs
                    ?: listOf() else listOf()
            }, listOf()
        )

    override suspend fun getAllPlan(
        s: String,
        e: String
    ): Either<Failure, List<ManufacturingPlanModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getAllPLan(configRepository.getCurrentRole().id,s,e),
            {
                if(it.success) it.content?.manufacturingPlans?.docs ?: listOf() else listOf()
            } ,
            listOf()
        )
    }

    override suspend fun getPlanById(id: String): Either<Failure, ManufacturingPlanDetailModel> {
        return requestApi(
            manufacturingManagerRemoteSource.getPlanById(id), {
                if (it.success) it.content?.manufacturingPlan
                    ?: ManufacturingPlanDetailModel() else ManufacturingPlanDetailModel()
            },
            ManufacturingPlanDetailModel()
        )
    }

    override suspend fun updatePlan(id: String, data: ParamUpdatePlan): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.updatePlan(id,data),{
                true
            } , true
        )
    }

    override suspend fun getApprovesOfPlanByRole(id: String): Either<Failure, List<UserRoleInOrganizationUnit>> {
        return requestApi(
            manufacturingManagerRemoteSource.getApprovesOfPlanByRole(id),
            {
                if(it.success) it.content?.users ?: listOf() else listOf()
            }, listOf()
        )
    }

    override suspend fun getGoodManageByRole(id: String): Either<Failure, List<GoodDetailModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getGoodManageByRole(id),{
                if(it.success) it.content?.goods ?: listOf() else listOf()
            }, listOf()
        )
    }

    override suspend fun getGoodByType(id: String): Either<Failure, List<GoodDetailModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getGoodByType(id),{
                if(it.success) it.content ?: listOf() else listOf()
            }, listOf()
        )
    }

    override suspend fun getFreeUsers(
        array: List<String>,
        role: String
    ): Either<Failure, List<SubUserBasicModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getFreeUsers(array, role),
            {
                if(it.success) it.content?.workers ?: listOf() else listOf()
            }, listOf()
        )
    }

    override suspend fun getAllUser(): Either<Failure, List<UserProfileResponse>> {
        return requestApi(
            manufacturingManagerRemoteSource.getAllUser(),
            {
                if(it.success) it.content ?: listOf() else listOf()

            } , listOf()
        )
    }

    override suspend fun getStock(): Either<Failure, List<StockModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getStock(),
            {
                if(it.success) it.content ?: listOf() else listOf()
            } , listOf()
        )
    }

    override suspend fun createExportBill(data : List<BillExportMaterialRequest>): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.createExportBills(data),{
                true
            },
            true
        )
    }

    override suspend fun updateCommand(
        data: RequestApproveCommand,
        id: String
    ): Either<Failure, Boolean> {
       return requestApi(
            manufacturingManagerRemoteSource.updateCommand(data,id),{
                true
            } , true
        )
    }

    override suspend fun createLots(data: List<RequestCreateLot>): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.createLots(data),{
                true
            } ,true
        )
    }

    override suspend fun getLotById(id: String): Either<Failure, ManufacturingLotDetailModel> {
        return requestApi(
            manufacturingManagerRemoteSource.getLotById(id),
            {
                if(it.success) it.content?.lot ?: ManufacturingLotDetailModel() else ManufacturingLotDetailModel()
            } , ManufacturingLotDetailModel()
        )
    }

    override suspend fun updateLot(id: String, data: RequestUpdateLot): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.updateLot(id, data),
            {
                true
            } , true
        )
    }

    override suspend fun updateInfoLot(id: String, data: RequestUpdateInfoLot): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.updateInfoLot(id, data),
            {
                true
            } , true
        )
    }

    override suspend fun getAllProductRequestInManufacturing(
        requestType: Int,
        requestFrom: String,
        from: String,
        to: String
    ): Either<Failure, List<ProductRequestManagementModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getAllProductRequestInManufacturing(requestType, requestFrom, from, to),{
                if(it.success) it.content?.requests?.docs ?: listOf() else listOf()
            },
            listOf()
        )
    }

    override suspend fun getListRequestByIds(ids: List<String>): Either<Failure, List<ProductRequestManagementModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getListRequestByIds(ids),{
                if(it.success) it.content?.request ?: listOf() else listOf()
            },
            listOf()
        )
    }

    override suspend fun createRequest(data: ParamCreateProductRequest): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.createRequest(data),{true},true
        )
    }

    override suspend fun createManyRequest(data: List<ParamCreateProductRequest>): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.createManyRequest(data),{true},true
        )
    }

    override suspend fun updateRequest(data : ParamCreateProductRequest, id:String): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.updateRequest(data,id),{true},true
        )
    }

    override suspend fun updateStatusRequest(data : ParamUpdateRequest, id:String): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.updateStatusRequest(data,id),{true},true
        )
    }

    override suspend fun createPlan(data: RequestManufacturingPlan): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.createPlan(data),{
                it.success
            },true
        )
    }

    override suspend fun finishCommand(data: RequestFinishCommand,id:String): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.finishCommand(data,id),{true},true
        )
    }

    override suspend fun updateQualityControlCommand(
        data: RequestQualityControl,
        id: String
    ): Either<Failure, Boolean> {
        return requestApi(
            manufacturingManagerRemoteSource.updateQualityControlCommand(data,id),{
                true
            } , true
        )    }

    override suspend fun getWorkScheduleOfMillByDate(
        mills: List<String>,
        start: String,
        end: String
    ): Either<Failure, List<WorkScheduleModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getWorkScheduleOfMillByDate(mills, start, end),{
                if(it.success) it.content?.workSchedules ?: listOf() else listOf()
            } , listOf()
        )
    }

    override suspend fun getSaleOrderByRole(id: String): Either<Failure, List<SubSaleOrder>> {
        return requestApi(
            manufacturingManagerRemoteSource.getSaleOrderByRole(id),
            {
                if(it.success) it.content?.salesOrders ?: listOf() else listOf()
            } , listOf()
        )
    }

    override suspend fun getAllManufacturingCommandsRemote(): Either<Failure, Boolean> {
        val r =
            requestApi(
                manufacturingManagerRemoteSource.getAllManufacturingCommand(configRepository.getCurrentRole().id,"",""),
                {
                    if (it.success) it.content?.manufacturingCommands?.docs
                        ?: listOf() else listOf()
                }, listOf()
            )
        if (r.isRight) {
            val t = r.getValue()
            val q = t.map {
                ManufacturingCommandEntity().apply {
                    _id = it._id
                    code = it.code
                    status = it.status
                    startDate = it.startDate
                    endDate = it.endDate
                    createdAt = it.createdAt
                    startTurn = it.startTurn
                    endTurn = it.endTurn
                    description = it.description
                    accountables = gson.toJson(it.accountables)
                    creator = gson.toJson(it.creator)
                    responsibles = gson.toJson(it.responsibles)
                    approvers = gson.toJson(it.approvers)
                    manufacturingMill = gson.toJson(it.manufacturingMill)
                    manufacturingPlan = gson.toJson(manufacturingPlan)
                    quantity = it.quantity
                    qualityControlStaffs = gson.toJson(it.qualityControlStaffs)
                    good = gson.toJson(it.good)
                    finishedProductQuantity = it.finishedProductQuantity
                    finishedTime = it.finishedTime
                    substandardProductQuantity = it.substandardProductQuantity
                }
            }
            manufacturingCommandLocalSource.savesAndDelete(q, configRepository.getDBName())
            return Either.Right(true)

        } else return Either.Left(Failure.ResponseError())
    }


    override suspend fun getAllCommandsFromDb(): List<ManufacturingCommandEntity> {
        return manufacturingCommandLocalSource.getAll(configRepository.getDBName())
    }

    override suspend fun getAllLotsFromDb(): List<ManufacturingLotEntity> {
        return manufacturingLotLocalSource.getAll(configRepository.getDBName())
    }

    override suspend fun getAllMillsFromDb(): List<ManufacturingMillEntity> {
        return manufacturingMillLocalSource.getAll(configRepository.getDBName())
    }

    override suspend fun getAllOrganizationUnitRemote(): Either<Failure, List<OrganizationUnit>> {
        return requestApi(
            manufacturingManagerRemoteSource.getAllOrganizationUnit(),
            {
                it.content?.list ?: listOf()
            },
            listOf()
        )
    }

    override suspend fun getAllRolesRemote(): Either<Failure, List<RoleModel>> {
        return requestApi(
            loginRemoteSource.getAllRoleRemote(),
            {
                if (it.success) it.content else listOf()
            }, listOf()
        )
    }

    override suspend fun getManufacturingWorkById(id: String): Either<Failure, ManufacturingWorkDetailModel> {
        return requestApi(
            manufacturingManagerRemoteSource.getManufacturingWorkById(id),
            {
                if (it.success) it.content?.manufacturingWorks
                    ?: ManufacturingWorkDetailModel() else ManufacturingWorkDetailModel()
            }, ManufacturingWorkDetailModel()
        )
    }

    override suspend fun getManufacturingCommandById(id: String): Either<Failure, ManufacturingCommandModel> {
        return requestApi(
            manufacturingManagerRemoteSource.getManufacturingCommandById(id),
            {
                if(it.success) it.content?.manufacturingCommand ?: ManufacturingCommandModel() else ManufacturingCommandModel()
            },
            ManufacturingCommandModel()
        )
    }

    override suspend fun getInventoryGoodByIds(ids: List<String>): Either<Failure, List<InventoryGoodWrap>> {
        return requestApi(
            manufacturingManagerRemoteSource.getInventoryGood(ids),
            {
                if(it.success) it.content ?: listOf() else listOf()
            },
            listOf()
        )
    }

    override suspend fun getBillByCommand(id: String): Either<Failure, List<BillByCommandModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getBillByCommand(id),
            {
                if(it.success) it.content ?: listOf() else listOf()
            }, listOf()
        )
    }
}