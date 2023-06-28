package com.example.test.dxworkspace.data.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandEntity
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotEntity
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillEntity
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.role.RoleModel
import com.example.test.dxworkspace.data.local.datasource.ManufacturingCommandLocalSource
import com.example.test.dxworkspace.data.local.datasource.ManufacturingLotLocalSource
import com.example.test.dxworkspace.data.local.datasource.ManufacturingMillLocalSource
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.LoginRemoteSource
import com.example.test.dxworkspace.data.remote.datasource.ManufacturingManagerRemoteSource
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingManagerRepository
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

    override suspend fun getAllManufacturingLotsRemote(): Either<Failure, List<ManufacturingLotModel>> {
        return requestApi(
            manufacturingManagerRemoteSource.getAllManufacturingLots(configRepository.getCurrentRole().id),
            {
                if (it.success) it.content?.lots?.docs ?: listOf() else listOf()
            },
            listOf()
        )
    }

    override suspend fun getAllAndSaveManufacturingLotsRemote(): Either<Failure, Boolean> {
        val r = requestApi(
            manufacturingManagerRemoteSource.getAllManufacturingLots(configRepository.getCurrentRole().id),
            {
                if (it.success) it.content?.lots?.docs ?: listOf() else listOf()
            },
            listOf()
        )
        if (r.isRight) {
            val t = r.getValue()
            val q = t.map {
                ManufacturingLotEntity().apply {
                    _id = it._id
                    code = it.code
                    type = it.type
                    description = it.description
                    createdAt = it.createdAt
                    productType = it.productType
                    status = it.status
                    expirationDate = it.expirationDate
                    creator = gson.toJson(it.creator)
                    bills = it.bills?.joinToString(",") ?: ""
                    passedQualityControl = it.passedQualityControl
                    originalQuantity = it.originalQuantity
                    quantity = it.quantity
                    good = gson.toJson(it.good)
                }
            }
            manufacturingLotLocalSource.savesAndDelete(q, configRepository.getDBName())
            return Either.Right(true)
        } else return Either.Left(Failure.ResponseError())
    }

    override suspend fun getAllManufacturingCommandsRemoteNew(): Either<Failure, List<ManufacturingCommandModel>> =
        requestApi(
            manufacturingManagerRemoteSource.getAllManufacturingCommand(configRepository.getCurrentRole().id),
            {
                if (it.success) it.content?.manufacturingCommands?.docs
                    ?: listOf() else listOf()
            }, listOf()
        )

    override suspend fun getAllManufacturingCommandsRemote(): Either<Failure, Boolean> {
        val r =
            requestApi(
                manufacturingManagerRemoteSource.getAllManufacturingCommand(configRepository.getCurrentRole().id),
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
}