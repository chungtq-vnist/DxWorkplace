package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.data.remote.api.DxApi
import retrofit2.Call
import javax.inject.Inject

class LoginRemoteSource @Inject constructor(val api : DxApi) {
    fun login(param : LoginRequestRaw) : Call<LoginResponseRaw> = api.loginByPassword(param)

    fun getLinksCanAccess(roleId : String , userId : String) = api.getLinkThatRoleCanAccess(roleId,userId)

    fun getComponentCanAccess(roleId : String , userId : String) = api.getComponentCanAccess(roleId,userId)

    fun logout() : Call<Unit> = api.logout()

    fun getProfile(userId: String) = api.getProfileUser(userId)

    suspend fun getAllRoleRemote() = api.getAllRoles()

}