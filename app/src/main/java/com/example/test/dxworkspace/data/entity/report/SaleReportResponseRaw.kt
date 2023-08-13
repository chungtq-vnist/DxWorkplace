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
    var cancelMoney : Double = 0.0 ,
    var numberOrder : Long = 0L ,
    var numberNewQuote : Long = 0L ,
    var numberNewOrder : Long = 0L ,
    var numberNewQuoteSuccess : Long = 0L ,
    var numberNewOrderSuccess : Long = 0L ,
    var numberCancelOrder : Long = 0L ,
    var numberPurchaseOrder : Long = 0L,
    var expense : Double = 0.0,
    var totalMoneyNewSaleOrder : Double = 0.0 ,
    var totalMoneyNewPurchaseOrder :Double = 0.0,

) {
    override fun equals(other: Any?): Boolean {
        if(other is SaleReportModel){
            return other.revenue == revenue && other.cancelMoney == cancelMoney && other.numberOrder == numberOrder && other.numberNewQuote == numberNewQuote
                    && other.numberNewOrder == numberNewOrder && other.numberNewQuoteSuccess == numberNewQuoteSuccess && other.numberCancelOrder == numberCancelOrder
                    && other.numberPurchaseOrder == numberPurchaseOrder
                    && other.expense == expense && other.totalMoneyNewSaleOrder == totalMoneyNewSaleOrder && other.totalMoneyNewPurchaseOrder == totalMoneyNewPurchaseOrder
        } else return false
    }
}
