package com.example.test.dxworkspace.data.remote.api

import com.example.test.dxworkspace.data.entity.component.ComponentResponseRaw
import com.example.test.dxworkspace.data.entity.link.LinkResponseRaw
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.data.entity.user.UserProfileResponseRaw
import retrofit2.Call
import retrofit2.http.*

interface LoginApi {
    @POST("/auth/login")
    fun loginByPassword(@Body param: LoginRequestRaw): Call<LoginResponseRaw>

    @GET("/auth/get-links-that-role-can-access/{roleId}")
    fun getLinkThatRoleCanAccess(
        @Path("roleId") roleId: String,
        @Query("userId") userId: String,
    ): Call<LinkResponseRaw>

    @GET("/component/components")
    fun getComponentCanAccess(
        @Query("currentRole") currentRole: String,
        @Query("userId") userId: String,
    ): Call<ComponentResponseRaw>

    @GET("/component/components")
    fun getAllComponents(
    ): Call<ComponentResponseRaw>

    @GET("/auth/logout")
    fun logout(): Call<Unit>

    @GET("/auth/get-profile/{userId}")
    fun getProfileUser(
        @Path("userId") userId: String
    ) : Call<UserProfileResponseRaw>


}