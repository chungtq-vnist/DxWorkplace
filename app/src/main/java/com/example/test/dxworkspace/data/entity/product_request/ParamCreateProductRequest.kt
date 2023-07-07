package com.example.test.dxworkspace.data.entity.product_request

import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.presentation.model.menu.SubLot

interface UpdateProductRequest
data class ParamCreateProductRequest(
    var approvers : MutableList<ParamInformation>? = mutableListOf(),
    var code : String ="",
    var description :String ="",
    var desiredTime  :String="",
    var goods : MutableList<ParamGood>? = mutableListOf(),
    var manufacturingWork :String ="",
    var requestType :Int = 0,
    var stock :String ="",
    var status :Int = 0,
    var type :Int = 0,
    var orderUnit : String? = "",
    var commandId : String? ="",
    var lotId :String? =""
) :UpdateProductRequest
data class ParamGood(
    var good :String ="",
    var quantity :String ="",
    var lots : MutableList<SubLot> = mutableListOf()
)

data class ParamApprover(
    var approvedTime : String? = null,
    var approver :String =""
)
data class ParamInformation (
    val information: MutableList<ParamApprover>? = mutableListOf(),
    val approveType: Int? = 0,
)

data class ParamUpdateRequest(
    var approveType : Int?,
    var approvedUser :String?,
    var status :Int?
) : UpdateProductRequest