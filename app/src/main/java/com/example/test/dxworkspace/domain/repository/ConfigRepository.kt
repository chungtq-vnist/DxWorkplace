package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.data.entity.login.RoleResponse
import com.example.test.dxworkspace.data.entity.login.RoleResponseRaw
import com.example.test.dxworkspace.data.entity.login.UserResponseRaw
import com.example.test.dxworkspace.data.entity.role.RoleEntity

interface ConfigRepository {
    fun getDBName() : String
    fun getCurrentRole() : RoleResponse
    fun getUser() : UserResponseRaw
}