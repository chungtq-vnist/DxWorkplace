package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.*
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.mapWithName
import com.example.test.dxworkspace.domain.usecase.manufacturing_dashboard.*
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetAllManufacturingWorkRemoteUsecase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetManufacturingWorkUseCase
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class DashboardControlManufacturingViewModel @Inject constructor(
    private val getNumberOfPlanByStatusUseCase: GetNumberOfPlanByStatusUseCase,
    private val getAllManufacturingWorkRemoteUsecase: GetAllManufacturingWorkRemoteUsecase,
    private val getNumberOfPlanByProgressUseCase: GetNumberOfPlanByProgressUseCase,
    private val getAllManufacturingWorkUseCase: GetManufacturingWorkUseCase,
    private val getNumberOfCommandByProgressUseCase: GetNumberOfCommandByProgressUseCase,
    private val getNumberOfCommandByStatusUseCase: GetNumberOfCommandByStatusUseCase,
    private val getNumberOfRequestByStatusUseCase: GetNumberOfRequestByStatusUseCase,
    private val getNumberOfRequestByTypeUseCase: GetNumberOfRequestByTypeUseCase,
    private val getReportGoodsQualityUseCase: GetReportGoodsQualityUseCase
) : BaseViewModel() {

//    var listManufacturingWorks = MutableLiveData<List<ManufacturingWorkEntity>>()

    var numberOfPlanByStatus = MutableLiveData<DashboardManufacturingPlanByStatus>()
    var numberOfPlanByProgress = MutableLiveData<DashboardManufacturingPlanByProgress>()

    var numberOfCommandByStatus = MutableLiveData<DashboardManufacturingCommandByStatus>()
    var numberOfCommandByProgress = MutableLiveData<DashboardManufacturingCommandByProgress>()

    var numberOfRequestByStatus = MutableLiveData<DashboardManufacturingRequestByStatus>()
    var numberOfRequestByType = MutableLiveData<DashboardManufacturingRequestByType>()

    var listGoods = MutableLiveData<List<QualityGoodsCompare>>()
    var listGoodsCompare = MutableLiveData<List<QualityGoods>>()
    val optionSelect: MutableLiveData<String> = MutableLiveData()
    val valueSelect: MutableLiveData<String> = MutableLiveData()
    fun setDataSelect(row: String?, money: String?) {
        valueSelect.value = money ?: "0"
        optionSelect.value = row ?: "-1"
    }


    var listWorksSelected = mutableListOf<ManufacturingWorkSelect>()
    var listAllWorks = mutableListOf<ManufacturingWorkSelect>()

    fun getAllManufacturingWorkCanManage(){
        getAllManufacturingWorkUseCase(""){
            it.either({handleFailure(it)},{
//                listManufacturingWorks.value = it
                listAllWorks = it.map { f -> f.mapWithName() }.toMutableList()
            })
        }
    }

    fun getNumberOfPlanByStatus(role : String , work : List<String>? ,from: String? , to:String?){
        getNumberOfPlanByStatusUseCase(ModelRequestDashboardPlan(role,work,from,to)){
            it.either({
                      handleFailure(it)
            },{
                numberOfPlanByStatus.value = it
            })
        }
    }

    fun getNumberOfPlanByProgress(role : String , work : List<String>? ,from: String? , to:String?){
        getNumberOfPlanByProgressUseCase(ModelRequestDashboardPlan(role,work,from,to)){
            it.either({
                      handleFailure(it)
            },{
                numberOfPlanByProgress.value = it
            })
        }
    }

    fun getNumberOfCommandByStatus(role : String , work : List<String>? ,from: String? , to:String?){
        getNumberOfCommandByStatusUseCase(ModelRequestDashboardPlan(role,work,from,to)){
            it.either({
                      handleFailure(it)
            },{
                numberOfCommandByStatus.value = it
            })
        }
    }

    fun getNumberOfCommandByProgress(role : String , work : List<String>? ,from: String? , to:String?){
        getNumberOfCommandByProgressUseCase(ModelRequestDashboardPlan(role,work,from,to)){
            it.either({
                      handleFailure(it)
            },{
                numberOfCommandByProgress.value = it
            })
        }
    }


    fun getNumberOfRequestByStatus(role : String , work : List<String>? ,from: String? , to:String?){
        getNumberOfRequestByStatusUseCase(ModelRequestDashboardPlan(role,work,from,to)){
            it.either({
                      handleFailure(it)
            },{
                numberOfRequestByStatus.value = it
            })
        }
    }

    fun getNumberOfRequestByType(role : String , work : List<String>? ,from: String? , to:String?){
        getNumberOfRequestByTypeUseCase(ModelRequestDashboardPlan(role,work,from,to)){
            it.either({
                      handleFailure(it)
            },{
                numberOfRequestByType.value = it
            })
        }
    }

    fun getReportGoodsQuality(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ) {
        getReportGoodsQualityUseCase(ModelRequestDashboardGoodQuality(role,work,from,to,fromCompare,toCompare)){
            it.either({
                handleFailure(it)
            },{
                listGoods.value = it
            })

        }
    }



}