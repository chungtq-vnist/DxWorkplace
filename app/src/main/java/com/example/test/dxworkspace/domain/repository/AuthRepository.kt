package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.data.entity.user.UserEntity

interface AuthRepository : Repository {
    fun login(param : LoginRequestRaw) : Either<Failure, LoginResponseRaw>

    suspend fun getLinksCanAccessRemote(roleId : String , userId : String )

    suspend fun getLinksCanAccess(dbName : String) : List<LinkEntity>

    suspend fun getComponentsCanAccess(dbName : String) : List<ComponentEntity>

    suspend fun getComponentCanAccessRemote(roleId: String , userId: String)

    suspend fun logout() : Either<Failure, Boolean>

    suspend fun getUserProfile(userId : String)


}