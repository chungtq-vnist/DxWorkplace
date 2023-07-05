package com.example.test.dxworkspace.data.entity.manufacturing_command

import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel

data class ManufacturingCommandDetailRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: ManufacturingCommandDetailWrap? = ManufacturingCommandDetailWrap()
)

data class ManufacturingCommandDetailWrap(
    val manufacturingCommand: ManufacturingCommandModel = ManufacturingCommandModel()
)

//data class ManufacturingCommandDetailModel(
//    var _id : String = "",
//    var code : String = "",
//    var status : Int = 0, //  Trạng thái lệnh sản xuất: 1. Chờ phê duyệt || 2. Lệnh đã duyệt || 3. Lệnh đang được thực thi || 4. Đã hoàn thành || 5. Đã hủy || 6. Mới tạo
//    var startDate : String ="",
//    var endDate : String ="",
//    var createdAt : String ="",
//    var startTurn : Int = 0,
//    var endTurn : Int = 0,
//    var description : String? = "",
//    var accountables : List<SubUserBasicModel>? = listOf(),
//    var creator : SubUserBasicModel? = SubUserBasicModel(),
//    var responsibles : List<SubUserBasicModel>? = listOf(),
//    var approvers : List<SubApproverModel>? = listOf(),
//    var manufacturingMill :SubManufacturingMill? = SubManufacturingMill(),
//    var manufacturingPlan : SubManufacturingPlan? = SubManufacturingPlan(),
//    var quantity : Int = 0,
//    var qualityControlStaffs : List<SubQualityControlStaff>? = listOf(),
//    var good : SubGoodModel = SubGoodModel(),
//    var finishedProductQuantity : Int? = null,
//    var finishedTime : String? = null,
//    var substandardProductQuantity : Int? = null,
//)