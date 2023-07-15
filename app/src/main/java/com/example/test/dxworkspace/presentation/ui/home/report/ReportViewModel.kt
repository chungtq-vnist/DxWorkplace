package com.example.test.dxworkspace.presentation.ui.home.report

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.ModelRequestDashboardGoodQuality
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.QualityGoodsCompare
import com.example.test.dxworkspace.data.entity.report.*
import com.example.test.dxworkspace.domain.usecase.manufacturing_dashboard.GetReportGoodsQualityUseCase
import com.example.test.dxworkspace.domain.usecase.report.GetFinancialReportUseCase
import com.example.test.dxworkspace.domain.usecase.report.GetNumberPlanCompletedReportUseCase
import com.example.test.dxworkspace.domain.usecase.report.GetSaleReportUseCase
import com.example.test.dxworkspace.domain.usecase.report.GetWarehouseReportUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class ReportViewModel @Inject constructor(
    private val getFinancialReportUseCase: GetFinancialReportUseCase,
    private val getSaleReportUseCase: GetSaleReportUseCase,
    private val getNumberPlanCompletedReportUseCase: GetNumberPlanCompletedReportUseCase,
    private val getReportGoodsQualityUseCase: GetReportGoodsQualityUseCase,
    private val getWarehouseReportUseCase : GetWarehouseReportUseCase,

    ) : BaseViewModel() {

    val isSuccess = MutableLiveData<Boolean>()
    val financialData = MutableLiveData<FinancialReportModel>()
    val financialDataCompare = MutableLiveData<FinancialReportModel>()
    val saleData = MutableLiveData<SaleReportModel>()
    val saleDataCompare = MutableLiveData<SaleReportModel>()
    val planData = MutableLiveData<PlanCompletedOnScheduleModel>()
    val planDataCompare = MutableLiveData<PlanCompletedOnScheduleModel>()
    var listGoods = MutableLiveData<List<QualityGoodsCompare>>()
    val listDataWarehouseReport = MutableLiveData<List<WarehouseReportModel>>()
    val statusReport = MutableLiveData<String>()






    fun getFinancialReport(param: ReportRequestModel) {
        getFinancialReportUseCase(param) {
            it.either({
                handleFailure(it)
            }, {
                financialData.value =
                    if (it.listAll.isNullOrEmpty()) FinancialReportModel() else it.listAll.first()
                financialDataCompare.value =
                    if (it.listAllCompare.isNullOrEmpty()) FinancialReportModel() else it.listAllCompare.first()
                isSuccess.value = true
            })
        }
    }

    fun getSaleReport(param: ReportRequestModel) {
        getSaleReportUseCase(param) {
            it.either({
                handleFailure(it)
            }, {
                saleData.value =
                    if (it.listAll.isNullOrEmpty()) SaleReportModel() else it.listAll.first()
                saleDataCompare.value =
                    if (it.listAllCompare.isNullOrEmpty()) SaleReportModel() else it.listAllCompare.first()
                isSuccess.value = true
            })
        }
    }

    fun getNumberPlanCompletedOnSchedule(param: ReportRequestModel){
        getNumberPlanCompletedReportUseCase(param){
            it.either({
                handleFailure(it)
            },{
                planData.value = if(it.data.isNullOrEmpty()) PlanCompletedOnScheduleModel() else it.data.first()
                planDataCompare.value = if(it.dataCompare.isNullOrEmpty()) PlanCompletedOnScheduleModel() else it.dataCompare.first()
                isSuccess.value = true
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
        getReportGoodsQualityUseCase(ModelRequestDashboardGoodQuality(role,null,from,to,fromCompare,toCompare)){
            it.either({
                handleFailure(it)
            },{
                listGoods.value = it
                isSuccess.value = true
            })

        }
    }

    fun getListDataReportWarehouse(params: ReportRequestModel){
        getWarehouseReportUseCase(params){
            it.either({
                handleFailure(it)
                statusReport.value = "FAIL"
            },{
                listDataWarehouseReport.value = it
                statusReport.value = "DONE"
            })
        }
    }
}