package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkResponseRaw
import com.example.test.dxworkspace.data.entity.version.VersionDiff
import com.example.test.dxworkspace.data.entity.version.VersionRequest

interface ManufacturingWorkRepository : Repository {

    suspend fun getAllManufacturingWorks()

    suspend fun getAllManufacturingWorksRemote() :  Either<Failure, List<ManufacturingWorkModel>>

    suspend fun getManufacturingWorkWithIds(ids : VersionRequest) :  Either<Failure, ManufacturingWorkResponseRaw>

    suspend fun handleCompareVersion(versionDiff: VersionDiff)

    suspend fun getListIdFromVersion(v : Int) : Either<Failure,List<String>>

    suspend fun getAllFromDb() : List<ManufacturingWorkEntity>

}