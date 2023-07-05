package com.example.test.dxworkspace.data.entity.product_request

import com.example.test.dxworkspace.data.entity.good.GoodModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect

data class ProductRequestManagementResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content :ProductRequestManagementResponse? = ProductRequestManagementResponse()
)

data class ProductRequestManagementResponse(
    val requests :ProductRequestManagementWrap = ProductRequestManagementWrap()
)

data class ProductRequestManagementWrap(
    val docs :List<ProductRequestManagementModel>? = listOf()
)

data class ProductRequestManagementModel(
    val requestType: Int = 0,
    val type: Int = 0,
    val status: Int = 0,
    val _id: String ="",
    val code: String ="",
    var creator : SubUserBasicModel? =SubUserBasicModel(),
    var approvers : List<ApproverElement>? = listOf(),
    var goods : List<GoodInfomation>? = listOf(),
    var manufacturingWork : ManufacturingWorkSelect? = null,
    var stock : StockModel? = StockModel(),
    var toStock : StockModel? = StockModel(),
    var createdAt : String ="",
    var desiredTime : String? ="",
//    var requestingDepartment
    var orderUnit :OrderUnit? = OrderUnit(),
    var supplier : OrderUnit? = null,
    var customer : OrderUnit? = null,
    var bill : SubBill? = null,
    var purchaseOrder : SubBill? = null,
    var saleOrder : SubBill? = null,

)

data class OrderUnit(
    var _id :String ="",
    var name :String =""
)


data class SubBill(
    var _id :String ="",
    var code :String =""
)
data class ApproverElement (
    val information: List<Information>? = listOf(),
    val approveType: Int = 0,
    val _id: String =""
)

data class Information (
    val approver: SubUserBasicModel,
    val approvedTime: String? = "",
    val _id: String ="",
    val note: String? = ""
)

data class GoodInfomation(
    var good : GoodModel = GoodModel(),
    var quantity : Int? =0 ,
    var _id : String ="",
    var lots : List<ManufacturingLotModel>? = listOf()
)

