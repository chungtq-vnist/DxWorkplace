package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.bill.BillByCommandModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.*
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetAllManufacturingWorkRemoteUsecase
import com.example.test.dxworkspace.presentation.model.menu.*
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class ManufacturingCommandViewModel @Inject constructor(
    private val getAllManufacturingMillRemoteUseCase: GetAllManufacturingMillRemoteUseCase,
    private val configRepository: ConfigRepository,
    private val getAllManufacturingWorkRemoteUsecase: GetAllManufacturingWorkRemoteUsecase,
    private val getAllOrganizationUnitRemoteUseCase: GetAllOrganizationUnitRemoteUseCase,
    private val getManufacturingMillByIdUseCase: GetManufacturingMillByIdUseCase,
    private val getManufacturingCommandUseCase: GetManufacturingCommandUseCase,
    private val getManufacturingCommandByIdUseCase: GetManufacturingCommandByIdUseCase,
    private val getBillByCommandUseCase: GetBillByCommandUseCase,
    private val getInventoryGoodByIdsUseCase: GetInventoryGoodByIdsUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getAllStockUseCase: GetAllStockUseCase,
    private val createExportBillsUseCase: CreateExportBillsUseCase,
    private val updateCommandUseCase: UpdateCommandUseCase,
    private val updateQualityControlCommandUseCase: UpdateQualityControlCommandUseCase,
    private val createdLotsUseCase: CreatedLotsUseCase,
    private val finishCommandUseCase: FinishCommandUseCase,
    private val getListProductRequestByIdsUseCase: GetListProductRequestByIdsUseCase,
) : BaseViewModel() {
    val listCommand = MutableLiveData<List<ManufacturingCommandModel>>()
    val commandDetail = MutableLiveData<ManufacturingCommandModel>()
    val listBill = MutableLiveData<List<BillByCommandModel>>()
    val listInventory = MutableLiveData<List<InventoryGoodWrap>>()
    val listUser = MutableLiveData<List<UserProfileResponse>>()
    val listStock = MutableLiveData<List<StockModel>>()
    val statusExportBill = MutableLiveData<Boolean>()
    val statusCancelCommand = MutableLiveData<Boolean>()
    val statusStartCommand = MutableLiveData<Boolean>()
    val statusUpdate = MutableLiveData<Boolean>()
    val listRequest = MutableLiveData<List<ProductRequestManagementModel>>()

    fun getAllCommand(start : String , end : String){
        showLoading(true)
        getManufacturingCommandUseCase( Pair(start,end)){
            it.either({
                showLoading(false)
            },{
                showLoading(false)
                listCommand.value = it
            })
        }
    }

    fun getManufacturingCommandById(id : String){
        showLoading(true)
        getManufacturingCommandByIdUseCase(id){
            it.either({
                handleFailure(it)
                showLoading(false)
            },{
                showLoading(false)
                commandDetail.value = it
            })
        }
    }

    fun getBillByCommand(id : String){
        getBillByCommandUseCase(id){
            it.either({
                handleFailure(it)
                listBill.value = listOf()
            },{
                listBill.value = it
            })
        }
    }

    fun getRequestExportMaterialOfCommand(ids :List<String>){
        getListProductRequestByIdsUseCase(ids){
            it.either({
                listRequest.value = listOf()
            },{
                listRequest.value = it
            })
        }
    }

    fun getInventoryByGoods(ids : List<String>){
        getInventoryGoodByIdsUseCase(ids){
            it.either({
                handleFailure(it)
                listInventory.value = listOf()
            },{
                listInventory.value = it
            })
        }
    }
    fun getAllUser(){
        getAllUsersUseCase(""){
            it.either({
                listUser.value = listOf()
            },{
                listUser.value = it
            })
        }
    }

    fun getStock(){
        getAllStockUseCase(""){
            it.either({
                      listStock.value = listOf()
            },{
                listStock.value = it
            })
        }

    }

    fun createExportBills(data : List<BillExportMaterialRequest>){
        createExportBillsUseCase(data){
            it.either({
            },{
            })
        }
    }

    fun updateCommand(data : RequestApproveCommand, id : String){
        updateCommandUseCase(Pair(data,id)){
            it.either({
                statusExportBill.value = false
            },{
                statusExportBill.value = true
            })
        }
    }

    fun cancelCommand(data : RequestApproveCommand, id : String) {
        showLoading(true)
        updateCommandUseCase(Pair(data,id)){
            it.either({
                showLoading(false)
                statusCancelCommand.value = false
            },{
                showLoading(false)
                statusCancelCommand.value = true
            })
        }
    }

    fun startCommand(data : RequestApproveCommand, id : String){
        showLoading(true)
        updateCommandUseCase(Pair(data,id)){
            it.either({
                showLoading(false)
                statusStartCommand.value = false
            },{
                showLoading(false)
                statusStartCommand.value = true
            })
        }
    }

    fun updateQualityControlCommand(data : RequestQualityControl, id : String){
        showLoading(true)
        updateQualityControlCommandUseCase(Pair(data,id)){
            it.either({
                showLoading(false)
                statusUpdate.value = false
            },{
                showLoading(false)
                statusUpdate.value = true
            })
        }
    }

    fun createLot(data : List<RequestCreateLot>){
        showLoading(true)
        createdLotsUseCase(data){
            it.either({

            },{})
        }
    }
    fun finishCommand(data : RequestFinishCommand , id : String){
        finishCommandUseCase(Pair(data,id)){

            it.either({
                showLoading(false)
                statusUpdate.value = false
            },{
                showLoading(false)
                statusUpdate.value = true
            })
        }
    }
}