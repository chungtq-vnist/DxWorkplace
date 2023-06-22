package com.example.test.dxworkspace.data.repository

import android.content.SharedPreferences
import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.version.VersionEntity
import com.example.test.dxworkspace.data.entity.version.VersionResponse
import com.example.test.dxworkspace.data.local.datasource.VersionLocalSource
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.repository.VersionRepository
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.get
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.VersionRemoteSource
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class VersionRepositoryImpl @Inject constructor(
    val authRepository: AuthRepository,
    val sharedPreferences: SharedPreferences,
    val configRepository: ConfigRepository,
    val versionRemoteSource: VersionRemoteSource,
    val versionLocalSource: VersionLocalSource,

    ) : VersionRepository {
    override suspend fun fetchAllDataLatest(dbName: String) {
        val roleId = configRepository.getCurrentRole().id
        val userId = configRepository.getUser().id
        coroutineScope {
            val components = async { authRepository.getComponentCanAccessRemote(roleId, userId) }
            val links = async { authRepository.getLinksCanAccessRemote(roleId, userId) }
            components.await()
            links.await()
        }
    }

    override suspend fun getAllVersions() : MutableList<VersionEntity> {
        val versions = mutableListOf<VersionEntity>()
        val result = requestApi(
            versionRemoteSource.getAllVersions(), {
                it.content
            },
            VersionResponse()
        )
        val res = if(result.isRight) result.getValue() else VersionResponse()
        versions.add(VersionEntity().apply {
            versionKey = Constants.VERSION_KEY.ROLE
            value = res.role
        })
        versions.add(VersionEntity().apply {
            versionKey = Constants.VERSION_KEY.USER
            value = res.user
        })
        versions.add(VersionEntity().apply {
            versionKey = Constants.VERSION_KEY.USER_ROLE
            value = res.userRole
        })
        versions.add(VersionEntity().apply {
            versionKey = Constants.VERSION_KEY.COMPONENT
            value = res.component
        })
        versions.add(VersionEntity().apply {
            versionKey = Constants.VERSION_KEY.LINK
            value = res.link
        })
        versions.add(VersionEntity().apply {
            versionKey = Constants.VERSION_KEY.PRIVILEGE
            value = res.privilege
        })
        versions.add(VersionEntity().apply {
            versionKey = Constants.VERSION_KEY.MANUFACTURINGWORK
            value = res.manufacturingWork
        })
        return versions
    }

    override suspend fun getAllVersionLocal(dbName: String): List<VersionEntity> {
        return versionLocalSource.getVersions(dbName)
    }

    override suspend fun save(c: VersionEntity, dbName: String) {
        versionLocalSource.saveVersion(c, dbName)
    }

    override suspend fun saves(c: List<VersionEntity>, dbName: String) {
        versionLocalSource.saveVersions(c, dbName)
    }
}