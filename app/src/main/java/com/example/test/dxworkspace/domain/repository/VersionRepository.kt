package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.data.entity.version.VersionEntity

interface VersionRepository {
    suspend fun fetchAllDataLatest(dbName : String)

    suspend fun getAllVersions(): MutableList<VersionEntity>

    suspend fun getAllVersionLocal(dbName: String) : List<VersionEntity>

    suspend fun save(c : VersionEntity , dbName: String)

    suspend fun saves(c : List<VersionEntity>, dbName: String)
}