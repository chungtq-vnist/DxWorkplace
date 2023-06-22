package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.component.ComponentResponse

interface ComponentRepository : Repository {

    suspend fun getAllComponentLocal(dbName :String) : List<ComponentEntity>

    suspend fun save(c : ComponentEntity , dbName: String)

    suspend fun getAllComponentsRemote()
}