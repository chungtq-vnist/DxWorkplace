package com.example.test.dxworkspace.data.repository

import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.local.datasource.ComponentLocalSource
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.api.requestDBWithoutFailure
import com.example.test.dxworkspace.data.remote.datasource.ComponentRemoteSource
import com.example.test.dxworkspace.domain.repository.ComponentRepository
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import javax.inject.Inject

class ComponentRepositoryImpl @Inject constructor(
    val componentLocalSource: ComponentLocalSource,
    val componentRemoteSource: ComponentRemoteSource,
    val configRepository: ConfigRepository
) : ComponentRepository {
    override suspend fun getAllComponentLocal(dbName: String): List<ComponentEntity> {
        return requestDBWithoutFailure(
            componentLocalSource.getAllComponentLocal(dbName),
            {
                it
            },
            listOf<ComponentEntity>()
        )
    }

    override suspend fun save(c: ComponentEntity, dbName: String) {
        componentLocalSource.save(c, dbName)
    }

    override suspend fun getAllComponentsRemote() {
        val result = requestApi(componentRemoteSource.getAllComponents(), {
            it.content.map { component ->
                ComponentEntity().apply {
                    _id = component._id
                    name = component.name
                    description = component.description
                }

            }

        }, mutableListOf())

        result.eitherAsync(
            {},{
                if(it.isNotEmpty()){
                    componentLocalSource.saves(it,configRepository.getDBName())
                }
            }
        )

    }


}