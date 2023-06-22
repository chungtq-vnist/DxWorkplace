package com.example.test.dxworkspace.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.test.dxworkspace.data.entity.login.RoleResponse
import com.example.test.dxworkspace.data.entity.login.RoleResponseRaw
import com.example.test.dxworkspace.data.entity.login.UserResponseRaw
import com.example.test.dxworkspace.data.entity.role.RoleEntity
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.get
import com.google.gson.Gson
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor(
    val sharedPreferences: SharedPreferences ,
    val context: Context,
    val gson : Gson
) : ConfigRepository {

    override fun getDBName(): String {
        val userId =  gson.fromJson(sharedPreferences[Constants.APLogin.CURRENT_USER,""],UserResponseRaw::class.java).id
        val roleId = getCurrentRole().id
        return "dx_workplace_${userId}_${roleId}"
    }

    override fun getCurrentRole(): RoleResponse {
        val role = sharedPreferences[Constants.APLogin.CURRENT_ROLE] ?: ""
        return if(role.isEmpty()) RoleResponse() else gson.fromJson(role,RoleResponse::class.java)
    }

    override fun getUser(): UserResponseRaw {
        val user = sharedPreferences[Constants.APLogin.CURRENT_USER] ?: ""
        return if(user.isEmpty()) UserResponseRaw() else gson.fromJson(user,UserResponseRaw::class.java)

    }
}