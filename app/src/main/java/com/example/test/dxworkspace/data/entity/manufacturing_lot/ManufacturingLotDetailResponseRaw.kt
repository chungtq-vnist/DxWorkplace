package com.example.test.dxworkspace.data.entity.manufacturing_lot

import com.example.test.dxworkspace.data.entity.manufacturing_command.*
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel

data class ManufacturingLotDetailResponseRaw (
        val success: Boolean = false,
        val messages: List<String> = emptyList(),
        val content: ManufacturingLotDetailResponse? = ManufacturingLotDetailResponse()
        )
data class ManufacturingLotDetailResponse(
        val lot : ManufacturingLotDetailModel? = ManufacturingLotDetailModel()
)

data class ManufacturingLotDetailModel(
        var _id : String ="",
        var code : String = "",
        var type : String? ="",
        var description : String? = "",
        var createdAt : String = "",
        var productType : Int? = 0,
        var status : Int = 0, // 1. Chưa lên đơn nhập kho 2. Đã lên đơn nhập kho 3. Đã nhập kho
        var expirationDate : String? = "",
        var creator : SubUserBasicModel? = SubUserBasicModel(),
        var bills : List<BillImportStock>? = listOf(),
        var passedQualityControl : Int? = 0,
        var originalQuantity : Int? = 0,
        var quantity : Int? = 0,
        var good : SubGoodsInLot? = SubGoodsInLot(),
        var manufacturingCommand : SubManufacturingCommandModel? = SubManufacturingCommandModel()
)
data class BillImportStock(
        var code : String ="",
        var _id : String =""
)

data class SubManufacturingCommandModel(
        var _id : String = "",
        var code : String = "",
        var status : Int = 0, //  Trạng thái lệnh sản xuất: 1. Chờ phê duyệt || 2. Lệnh đã duyệt || 3. Lệnh đang được thực thi || 4. Đã hoàn thành || 5. Đã hủy || 6. Mới tạo
        var startDate : String ="",
        var endDate : String ="",
        var createdAt : String ="",
        var startTurn : Int = 0,
        var endTurn : Int = 0,
        var description : String? = "",
        var accountables : List<SubUserBasicModel>? = listOf(),
        var creator : String? = "",
        var responsibles : List<SubUserBasicModel>? = listOf(),
        var manufacturingMill : SubManufacturingMill? = SubManufacturingMill(),
        var manufacturingPlan : String? = "",
        var quantity : Int = 0,
        var qualityControlStaffs : List<SubQualityControlStaff>? = listOf(),
        var good : SubGoodModel = SubGoodModel(),
        var finishedProductQuantity : Int? = null,
        var finishedTime : String? = null,
        var substandardProductQuantity : Int? = null,
)