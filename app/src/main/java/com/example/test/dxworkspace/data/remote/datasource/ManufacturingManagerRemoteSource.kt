package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.remote.api.DxApi
import javax.inject.Inject

class ManufacturingManagerRemoteSource @Inject constructor(val api: DxApi) {
    fun getAllManufacturingMills(currentRole : String) = api.getAllManufacturingMills(1, 100,currentRole)

    fun getManufacturingMillById(id : String ) = api.getManufacturingMillById(id)

    fun getAllManufacturingLots(currentRole: String) = api.getAllManufacturingLots(1,1000,currentRole)

    fun getAllManufacturingCommand(currentRole: String) = api.getAllManufacturingCommand(1,1000,currentRole)

    fun getAllOrganizationUnit() = api.getALlOrganizationUnits()

    fun getManufacturingWorkById(id : String) = api.getManufacturingWorkById(id)
}