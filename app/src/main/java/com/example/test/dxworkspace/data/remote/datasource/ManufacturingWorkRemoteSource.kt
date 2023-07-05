package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.entity.version.VersionRequest
import com.example.test.dxworkspace.data.remote.api.DxApi
import javax.inject.Inject

class ManufacturingWorkRemoteSource @Inject constructor(val api : DxApi) {
    fun getAllManufacturingWorks(currentRole : String? ) = api.getAllManufacturingWorks(currentRole)

    fun getListIdFromVersion(v : Int) = api.getListIdFromVersion(v)

    fun getManufacturingWorksWithIds(currentRole : String , ids : VersionRequest) = api.getManufacturingWorkWithIds(currentRole ,ids)
}