package com.example.test.dxworkspace.data.entity.product_request

data class ListProductRequestResponseRaw (
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content :ListProductRequestResponse? = ListProductRequestResponse()
)

data class ListProductRequestResponse(
    val request : List<ProductRequestManagementModel>? = listOf()
)