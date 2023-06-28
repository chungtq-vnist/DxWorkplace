package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandEntity
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotEntity
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillEntity
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.role.RoleModel

interface ManufacturingManagerRepository {
    suspend fun getALlManufacturingMillsRemote() : Either<Failure,List<ManufacturingMillModel>>

    suspend fun getManufacturingMillById(id : String) : Either<Failure,ManufacturingMillModel>

    suspend fun getAllManufacturingLotsRemote() : Either<Failure,List<ManufacturingLotModel>>

    suspend fun getALlAndSaveManufacturingMillsRemote() : Either<Failure,Boolean>

    suspend fun getAllAndSaveManufacturingLotsRemote() : Either<Failure,Boolean>

    suspend fun getAllManufacturingCommandsRemote() : Either<Failure , Boolean>
    suspend fun getAllManufacturingCommandsRemoteNew() : Either<Failure, List<ManufacturingCommandModel>>

    suspend fun getAllMillsFromDb() : List<ManufacturingMillEntity>
    suspend fun getAllLotsFromDb() : List<ManufacturingLotEntity>
    suspend fun getAllCommandsFromDb() : List<ManufacturingCommandEntity>

    suspend fun getAllOrganizationUnitRemote() : Either<Failure, List<OrganizationUnit>>

    suspend fun getAllRolesRemote() : Either<Failure, List<RoleModel>>

    suspend fun getManufacturingWorkById(id : String) : Either<Failure,ManufacturingWorkDetailModel>
}