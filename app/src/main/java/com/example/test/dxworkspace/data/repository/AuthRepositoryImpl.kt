package com.example.test.dxworkspace.data.repository

import android.content.SharedPreferences
import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.data.entity.login.UserResponseRaw
import com.example.test.dxworkspace.data.entity.user.UserEntity
import com.example.test.dxworkspace.data.entity.user.UserProfileResponseRaw
import com.example.test.dxworkspace.data.local.datasource.AuthLocalSource
import com.example.test.dxworkspace.data.local.datasource.ComponentLocalSource
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.api.requestDBWithoutFailure
import com.example.test.dxworkspace.data.remote.datasource.LoginRemoteSource
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.google.gson.Gson
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val loginRemoteSource: LoginRemoteSource ,
    val authLocalSource: AuthLocalSource ,
    val configRepository: ConfigRepository,
    val componentLocalSource: ComponentLocalSource,
    val gson: Gson,
    val sharedPreferences: SharedPreferences,
) : AuthRepository {
    override fun login(param: LoginRequestRaw): Either<Failure, LoginResponseRaw> = requestApi(
        loginRemoteSource.login(param),LoginResponseRaw()
    )

    override suspend fun getLinksCanAccessRemote(roleId: String, userId: String) {
        val result = requestApi(
            loginRemoteSource.getLinksCanAccess(roleId, userId),
            {
                it.content.map { link ->
                    LinkEntity().apply {
                        _id = link._id
                        url = link.url
                        description = link.description
                        category = link.category
                        createdAt = link.createdAt
                        updatedAt = link.updatedAt
                    }
                }
            },
            listOf<LinkEntity>()
        )
        result.eitherAsync(
            {},{
                if(it.isNotEmpty()){
                    authLocalSource.saveLinks(it,configRepository.getDBName())
                }
            }
        )
    }
    
    

    override suspend fun getComponentCanAccessRemote(roleId: String, userId: String) {
        val result = requestApi(
            loginRemoteSource.getComponentCanAccess(roleId, userId),
            {
                it.content.map { component ->
                    ComponentEntity().apply {
                        _id = component._id
                        name = component.name
                        description = component.description
                        deleteSoft = component.deleteSoft
                    }
                }
            },
            listOf()
        )
        result.eitherAsync(
            {},{
                if(it.isNotEmpty()){
                    componentLocalSource.saves(it,configRepository.getDBName())
                }
            }
        )
    }
    
    
    override suspend fun getLinksCanAccess(dbName: String): List<LinkEntity> {
        return requestDBWithoutFailure(
            authLocalSource.getAllLinksLocal(dbName) ,
            {it},
            listOf()
        )
    }

    override suspend fun getComponentsCanAccess(dbName: String): List<ComponentEntity> {
        return requestDBWithoutFailure(
            componentLocalSource.getAllComponentLocal(dbName) ,
            {it},
            listOf()
        )
    }

    override suspend fun logout() : Either<Failure, Boolean> {
        return requestApi(loginRemoteSource.logout() ,{  true} , false)
    }

    override suspend fun getUserProfile(userId: String) {
        val res = requestApi(loginRemoteSource.getProfile(userId),UserProfileResponseRaw())
        if(res.isRight){
            val user = res.getValue().content
            if(user.id.isNotEmpty()){
                val userNow = configRepository.getUser()
                val userUpdate = UserResponseRaw(
                    userNow.id,
                    user.name,
                    user.email,
                    user.avatar,
                    user.roles,
                    userNow.company
                )
                sharedPreferences[Constants.APLogin.CURRENT_USER] = gson.toJson(userUpdate)
            }
        }
    }

}