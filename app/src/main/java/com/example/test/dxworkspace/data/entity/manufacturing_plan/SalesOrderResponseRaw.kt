package com.example.test.dxworkspace.data.entity.manufacturing_plan

import com.example.test.dxworkspace.data.entity.manufacturing_command.SubSaleOrder

data class SalesOrderResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: SubSalesOrderManufacturing? = SubSalesOrderManufacturing()
)

data class SubSalesOrderManufacturing(
    var salesOrders: List<SubSaleOrder>? = listOf()

)