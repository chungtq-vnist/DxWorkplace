package com.example.test.dxworkspace.data.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkResponseRaw
import com.example.test.dxworkspace.data.entity.version.VersionDiff
import com.example.test.dxworkspace.data.entity.version.VersionEntity
import com.example.test.dxworkspace.data.entity.version.VersionRequest
import com.example.test.dxworkspace.data.local.datasource.ManufacturingWorkLocalSource
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.ManufacturingWorkRemoteSource
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.repository.VersionRepository
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.google.gson.Gson
import javax.inject.Inject

class ManufacturingWorkRepositoryImpl @Inject constructor(
    val configRepository: ConfigRepository,
    val manufacturingWorkRemoteSource: ManufacturingWorkRemoteSource,
    val manufacturingWorkLocalSource: ManufacturingWorkLocalSource,
    val versionRepository: VersionRepository,
    val gson: Gson
) : ManufacturingWorkRepository {
    override suspend fun getAllManufacturingWorks() {
        val r = requestApi(
            manufacturingWorkRemoteSource.getAllManufacturingWorks(configRepository.getCurrentRole().id),
            ManufacturingWorkResponseRaw()
        )
        if (r.isRight) {
            val t = r.getValue().content.allManufacturingWorks.docs ?: emptyList()
            val q = t.map {
                ManufacturingWorkEntity().apply {
                    _id = it._id
                    address = it.address
                    code = it.code
                    createdAt = it.createdAt
                    description = it.description
                    name = it.name
                    phoneNumber = it.phoneNumber
                    status = it.status
                    udpatedAt = it.udpatedAt
                    manufacturingMills =
                        it.manufacturingMills?.map { it._id }?.joinToString(",") ?: ""
                    manageRoles = it.manageRoles?.joinToString(",") ?: ""
                    organizationalUnit = gson.toJson(it.organizationalUnit)
                    turn = it.turn
                }
            }
            manufacturingWorkLocalSource.saves(q, configRepository.getDBName())
        }

    }
    override suspend fun getAllManufacturingWorksRemote(): Either<Failure, List<ManufacturingWorkModel>> {
        return requestApi(
            manufacturingWorkRemoteSource.getAllManufacturingWorks(configRepository.getCurrentRole().id),
            {
            if(it.success){
                it.content.allManufacturingWorks.docs ?: listOf()
            } else listOf()
            },
            listOf()
        )

    }

    override suspend fun getAllManufacturingWorksRemoteWithoutRole(): Either<Failure, List<ManufacturingWorkModel>> {
        return requestApi(
            manufacturingWorkRemoteSource.getAllManufacturingWorks(""),
            {
                if(it.success){
                    it.content.allManufacturingWorks.docs ?: listOf()
                } else listOf()
            },
            listOf()
        )
    }

    override suspend fun handleCompareVersion(versionDiff: VersionDiff) {
        val versionNow = versionDiff.versionRemote
        val v = getListIdFromVersion(versionDiff.versionDB)
        var ids = if (v.isRight) {
            v.getValue()
        } else emptyList()
        if (ids.isNotEmpty()) {
            val res = getManufacturingWorkWithIds(VersionRequest(ids))
            val listUpdate = if (res.isRight) {
                res.getValue().content.allManufacturingWorks.docs
            } else emptyList()
            val l = listUpdate?.map { it._id } ?: listOf()
            val listDelete = ids.filterNot { l.contains(it) }
            manufacturingWorkLocalSource.deletes(listDelete, configRepository.getDBName())

            if (listUpdate?.isNotEmpty() == true) {
                val q = listUpdate.map {
                    ManufacturingWorkEntity().apply {
                        _id = it._id
                        address = it.address
                        code = it.code
                        createdAt = it.createdAt
                        description = it.description
                        name = it.name
                        phoneNumber = it.phoneNumber
                        status = it.status
                        udpatedAt = it.udpatedAt
                        manufacturingMills =
                            it.manufacturingMills?.map { it._id }?.joinToString(",") ?: ""
                        manageRoles = it.manageRoles?.joinToString(",") ?: ""
                        organizationalUnit = gson.toJson(it.organizationalUnit)
                        turn = it.turn
                    }
                }
                manufacturingWorkLocalSource.saves(q, configRepository.getDBName())
            }
        }

        versionRepository.save(VersionEntity().apply {
            versionKey = Constants.VERSION_KEY.MANUFACTURINGWORK
            value = versionNow
        },configRepository.getDBName())
    }

    override suspend fun getListIdFromVersion(v: Int): Either<Failure, List<String>> {
        return requestApi(manufacturingWorkRemoteSource.getListIdFromVersion(v), {
            it.content
        }, listOf())
    }

    override suspend fun getManufacturingWorkWithIds(ids: VersionRequest): Either<Failure, ManufacturingWorkResponseRaw> {
        return requestApi(
            manufacturingWorkRemoteSource.getManufacturingWorksWithIds(
                configRepository.getCurrentRole().id,
                ids
            ),
            ManufacturingWorkResponseRaw()
        )
    }

    override suspend fun getAllFromDb(): List<ManufacturingWorkEntity> {
        return manufacturingWorkLocalSource.getAll(configRepository.getDBName())
    }
}