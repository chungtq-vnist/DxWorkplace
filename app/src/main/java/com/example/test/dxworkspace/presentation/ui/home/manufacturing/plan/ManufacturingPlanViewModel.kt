package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.UserRoleInOrganizationUnit
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleModel
import com.example.test.dxworkspace.domain.model.manufacturing_plan.RequestManufacturingPlan
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.*
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetAllManufacturingWorkRemoteUsecase
import com.example.test.dxworkspace.presentation.model.menu.WorkerScheduleRequest
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import com.google.gson.Gson
import javax.inject.Inject

class ManufacturingPlanViewModel  @Inject constructor(
    private val getAllManufacturingMillRemoteUseCase: GetAllManufacturingMillRemoteUseCase,
    private val configRepository: ConfigRepository,
    private val getAllManufacturingWorkRemoteUsecase: GetAllManufacturingWorkRemoteUsecase,
    private val getAllOrganizationUnitRemoteUseCase: GetAllOrganizationUnitRemoteUseCase,
    private val getManufacturingMillByIdUseCase: GetManufacturingMillByIdUseCase,
    private val getManufacturingCommandUseCase: GetManufacturingCommandUseCase,
    private val getManufacturingCommandByIdUseCase: GetManufacturingCommandByIdUseCase,
    private val getAllPlanUseCase: GetManufacturingPlanUseCase,
    private val getSaleOrderByRoleUseCase: GetSaleOrderByRoleUseCase,
    private val getApprovesOfPlanByRoleUseCase: GetApprovesOfPlanByRoleUseCase,
    private val getGoodManageByRoleUseCase: GetGoodManageByRoleUseCase,
    private val getInventoryGoodByIdsUseCase: GetInventoryGoodByIdsUseCase,
    private val getFreeUsersUseCase: GeFreeUsersUseCase,
    private val getWorkScheduleOfMillByDateUseCase: GeWorkScheduleOfMillByDateUseCase,
    private val getAllUser : GetAllUsersUseCase,
    private val gson: Gson,
    private val createManufacturingPlanUseCase: CreateManufacturingPlanUseCase
) : BaseViewModel() {
    val listPlan = MutableLiveData<List<ManufacturingPlanModel>>()
    val listMills = MutableLiveData<List<ManufacturingMillModel>>()
    val listUserApporvers = MutableLiveData<List<UserProfileResponse>>()
    val listGood = MutableLiveData<List<GoodDetailModel>>()
    val listInventory = MutableLiveData<List<InventoryGoodWrap>>()
    val listUser = MutableLiveData<List<UserProfileResponse>>()
    val listSchedule = MutableLiveData<List<WorkScheduleModel?>>()
    val listUserFree = MutableLiveData<List<SubUserBasicModel>>()
    val updateStatus = MutableLiveData<Boolean>()

    fun getListPlan(s : String , e : String ){
        getAllPlanUseCase(Pair(s,e)){
            showLoading(true)
            it.either({
                showLoading(false)
                handleFailure(it)
            },{
                showLoading(false)
                listPlan.value = it
            })
        }
    }

    // get list nguoi co the phe duyet ke hoach san xuat
    fun getListApprovers(id : String){
        getApprovesOfPlanByRoleUseCase(id){
            it.either({
                      listUserApporvers.value = listOf()
            },{
                listUserApporvers.value = it.filter{ !it.userId?.id.isNullOrEmpty() }.map { k -> UserProfileResponse(k.userId!!.id,k.userId!!.name,k.userId!!.email) }
            })
        }
    }

    fun getListSalesOrder(id : String){
        getSaleOrderByRoleUseCase(id){
            it.either({

            },{

            })
        }
    }

    fun getListGoodManage(id : String){
        getGoodManageByRoleUseCase(id){
            it.either({
                listGood.value = listOf()
            },{
                listGood.value = it
                if(listGood.value?.size ?: 0 > 0) getInventoryByGoods(listGood.value?.map { it._id } ?: listOf() )
            })
        }
    }

    fun getManufacturingMills() {
        showLoading(true)
        getAllManufacturingMillRemoteUseCase("") {
            it.either({
                showLoading(false)
            }, {
                showLoading(false)
                listMills.value = it.filter{it.status == 1}
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
        getAllUser(""){
            it.either({
                listUser.value = listOf()
            },{
                listUser.value = it
            })
        }
    }

    fun getWorkScheduleOfMillByDate(mills : List<String>, start : String , end: String ){
        getWorkScheduleOfMillByDateUseCase(Triple(mills,start,end)){
            it.either({

            },{
                listSchedule.value = it
            })
        }
    }

    fun getFreeUser(param : List<String>){
        getFreeUsersUseCase(Pair(param,configRepository.getCurrentRole().id)){
            it.either(
                {},{
                    listUserFree.value = it
                }
            )
        }
    }

    fun createManufacturingPlan(data : RequestManufacturingPlan){
        showLoading(true)
        createManufacturingPlanUseCase(data){
            it.either({
                showLoading(false)
                updateStatus.value = false
            },{
                showLoading(false)
                updateStatus.value = true
            })
        }
    }

}