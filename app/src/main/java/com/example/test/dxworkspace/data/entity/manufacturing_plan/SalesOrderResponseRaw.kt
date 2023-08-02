package com.example.test.dxworkspace.data.entity.manufacturing_plan

import com.example.test.dxworkspace.data.entity.manufacturing_command.SubSaleOrder
import com.example.test.dxworkspace.data.entity.product_request.GoodInfomation

data class SalesOrderResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: SalesOrderResponse? = SalesOrderResponse()
)

data class SubSalesOrderManufacturing(
    var salesOrders: List<SubSaleOrder>? = listOf()

)

data class SalesOrderResponse(
    var salesOrders: List<SalesOrderModel>? = listOf()
)

data class SalesOrderModel(
    val status: Int = 1,
    val _id: String? ="",
    val code: String ="",
    val goods: List<GoodInfomation>? = listOf(),


){
    override fun toString(): String {
        return code
    }
}
