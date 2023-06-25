package com.example.test.dxworkspace.presentation.ui.home.report

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.report.FinancialReportModel
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.domain.usecase.report.GetFinancialReportUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class ReportViewModel @Inject constructor(
    private val getFinancialReportUseCase: GetFinancialReportUseCase
) : BaseViewModel() {

    val isSuccess = MutableLiveData<Boolean>()
    val financialData = MutableLiveData<FinancialReportModel>()
    val financialDataCompare = MutableLiveData<FinancialReportModel>()

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
}