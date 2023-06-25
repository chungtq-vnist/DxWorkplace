package com.example.test.dxworkspace.data.entity.report

data class SaleReportResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : SaleReportContentResponse? = SaleReportContentResponse()
)

data class SaleReportContentResponse(
    val listAll : List<SaleReportModel>? = listOf(),
    val listAllCompare : List<SaleReportModel>? = listOf()
)

data class SaleReportModel(
    var revenue : Double = 0.0 ,
    var cancelOrderMoney : Double = 0.0 ,
    var numberOrder : Long = 0L ,
    var numberNewQuote : Long = 0L ,
    var numberNewOrder : Long = 0L ,
    var numberNewQuoteSuccess : Long = 0L ,
    var numberNewOrderSuccess : Long = 0L
)
