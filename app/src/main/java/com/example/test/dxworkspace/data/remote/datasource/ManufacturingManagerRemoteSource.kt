package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.data.entity.product_request.UpdateProductRequest
import com.example.test.dxworkspace.data.remote.api.DxApi
import com.example.test.dxworkspace.domain.model.manufacturing_plan.RequestManufacturingPlan
import com.example.test.dxworkspace.presentation.model.menu.*
import javax.inject.Inject

class ManufacturingManagerRemoteSource @Inject constructor(val api: DxApi) {
    fun getAllManufacturingMills(currentRole : String) = api.getAllManufacturingMills(1, 100,currentRole)

    fun getManufacturingMillById(id : String ) = api.getManufacturingMillById(id)

    fun getAllManufacturingLots(currentRole: String,from : String, to :String) = api.getAllManufacturingLots(1,1000,currentRole ,from,to)

    fun getAllManufacturingCommand(currentRole: String , startDate : String, endDate : String) = api.getAllManufacturingCommand(1,1000,currentRole,endDate,startDate)

    fun getAllOrganizationUnit() = api.getALlOrganizationUnits()

    fun getManufacturingWorkById(id : String) = api.getManufacturingWorkById(id)

    fun getManufacturingCommandById(id : String) = api.getManufacturingCommandById(id)

    fun getInventoryGood(id : List<String>) = api.getInventoryGood(id)

    fun getBillByCommand(id : String) = api.getBillOfCommand(id)

    fun getAllPLan(currentRole: String , startDate : String, endDate : String) = api.getAllPlan(endDate,startDate,currentRole)

    fun getApprovesOfPlanByRole(id : String) = api.getApprovesOfPlanByRole(id)

    fun getSaleOrderByRole(id : String) = api.getSaleOrderByRole(id)

    fun getGoodManageByRole(id : String) = api.getGoodManageByRole(id)
    fun getGoodByType(type : String) = api.getAllGoods(type)

    fun getFreeUsers(array : List<String> ,role : String) = api.getFreeUsers(array, role)

    fun getWorkScheduleOfMillByDate(mills : List<String>,start : String , end : String) = api.getWorkScheduleOfMillByDate(mills, start, end)

    fun getAllUser() = api.getAllUser()

    fun getStock() = api.getStocks()

    fun createExportBills( data : List<BillExportMaterialRequest>) = api.createExportBills(data)

    fun updateCommand(data : RequestApproveCommand , id : String) = api.updateCommand(id,data)

    fun updateQualityControlCommand(data : RequestQualityControl , id : String) = api.updateQualityControlCommand(id,data)

    fun finishCommand(data : RequestFinishCommand, id : String) = api.finishCommand(id,data)

    fun createLots(data : List<RequestCreateLot> ) = api.createLots(data)

    fun getLotById(id  : String) = api.getLotById(id)

    fun updateLot(id : String ,data : RequestUpdateLot) = api.updateLot(id,data)

    fun updateInfoLot(id : String ,data : RequestUpdateInfoLot) = api.updateInfoLot(id,data)

    fun getAllProductRequestInManufacturing(requestType : Int , requestFrom : String , from :String,to:String)
        = api.getAllProductRequestInManufacturing(requestType, requestFrom, from, to)

    fun createRequest(data : ParamCreateProductRequest) = api.createRequest(data)

    fun updateRequest(data : UpdateProductRequest , id:String) = api.updateRequest(id,data)

    fun createPlan(data : RequestManufacturingPlan) = api.createPlan(data)

}