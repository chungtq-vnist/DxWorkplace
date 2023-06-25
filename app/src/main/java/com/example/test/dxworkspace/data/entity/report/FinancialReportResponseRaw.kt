package com.example.test.dxworkspace.data.entity.report

data class FinancialReportResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : FinancialReportContentResponse? = FinancialReportContentResponse()
)

data class FinancialReportContentResponse(
    val listAll : List<FinancialReportModel>? = listOf(),
    val listAllCompare : List<FinancialReportModel>? = listOf()
)

data class FinancialReportModel(
    var revenue : Double? = 0.0 ,
    var expense : Double? = 0.0 ,
    var repair : Double? = 0.0 ,
    var cancelOrderMoney : Double? = 0.0 ,
    var numberOrder : Long? = 0L ,
    var buyEquipment : Double? = 0.0,
    var profit : Double? = 0.0 ,
)
