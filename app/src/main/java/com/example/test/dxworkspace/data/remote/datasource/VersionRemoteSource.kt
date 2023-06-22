package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.entity.component.ComponentResponseRaw
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.data.entity.version.VersionResponseRaw
import com.example.test.dxworkspace.data.remote.api.DxApi
import retrofit2.Call
import javax.inject.Inject

class VersionRemoteSource @Inject constructor(val api : DxApi) {

    fun getAllVersions() : Call<VersionResponseRaw> = api.getAllVersions()

}