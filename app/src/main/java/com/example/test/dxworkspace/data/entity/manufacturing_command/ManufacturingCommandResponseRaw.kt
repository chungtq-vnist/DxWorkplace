package com.example.test.dxworkspace.data.entity.manufacturing_command

import com.example.test.dxworkspace.data.entity.good.GoodModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotResponseWrap
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel

data class ManufacturingCommandResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: ManufacturingCommandResponseWrap? = ManufacturingCommandResponseWrap()
)

data class ManufacturingCommandResponseWrap(
    val manufacturingCommands: ManufacturingCommandResponse? = ManufacturingCommandResponse()
)

data class ManufacturingCommandResponse(
    val docs: List<ManufacturingCommandModel>? = listOf()
)

data class ManufacturingCommandModel(
    var _id : String = "",
    var code : String = "",
    var status : Int = 0,
    var startDate : String ="",
    var endDate : String ="",
    var createdAt : String ="",
    var startTurn : Int = 0 ,
    var endTurn : Int = 0 ,
    var description : String? = "",
    var accountables : List<SubUserBasicModel>? = listOf() ,
    var creator : SubUserBasicModel? = SubUserBasicModel(),
    var responsibles : List<SubUserBasicModel>? = listOf(),
    var approvers : List<SubApproverModel>? = listOf(),
    var manufacturingMill :SubManufacturingMill? = SubManufacturingMill(),
    var manufacturingPlan : SubManufacturingPlan? = SubManufacturingPlan(),
    var quantity : Int = 0,
    var qualityControlStaffs : List<SubQualityControlStaff>? = listOf(),
    var good : SubGoodModel = SubGoodModel(),
    var finishedProductQuantity : Int? = null,
    var finishedTime : String? = null,
    var substandardProductQuantity : Int? = null,
    )

data class SubManufacturingMill(
    var _id : String ="",
    var code : String ="",
    var name : String =""
)
data class SubManufacturingPlan(
    var _id : String ="",
    var code : String ="",
)

data class SubApproverModel(
    var approvedTime : String = "",
    var approver : SubUserBasicModel? = SubUserBasicModel(),

)

data class SubGoodModel(
    var _id: String? = "",
    var baseUnit: String? = "",
    var code: String? = "",
    var name: String? = "",
    var materials : List<SubMaterialModel>? = listOf()
)

data class SubMaterialModel(
    var good : GoodModel? = GoodModel(),
    var quantity : Int? = 0
)
data class SubQualityControlStaff(
    var status : Int = 0,
    var content : String? = "",
    var time : String? = "",
    var staff : SubUserBasicModel = SubUserBasicModel()
)