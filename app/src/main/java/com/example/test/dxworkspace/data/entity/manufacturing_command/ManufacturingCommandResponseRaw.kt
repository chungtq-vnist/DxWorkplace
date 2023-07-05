package com.example.test.dxworkspace.data.entity.manufacturing_command

import android.os.Parcelable
import com.example.test.dxworkspace.data.entity.good.GoodModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotResponseWrap
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import kotlinx.android.parcel.Parcelize

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

@Parcelize
data class ManufacturingCommandModel(
    var _id : String = "",
    var code : String = "",
    var status : Int = 0, //  Trạng thái lệnh sản xuất: 1. Chờ phê duyệt || 2. Lệnh đã duyệt || 3. Lệnh đang được thực thi || 4. Đã hoàn thành || 5. Đã hủy || 6. Mới tạo
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
    var lot : List<SubManufacturingLot>? = listOf(),
    var purchasingRequest : String? = ""
    ) : Parcelable

@Parcelize
data class SubManufacturingLot(
    val code : String? ="",
    val manufacturingCommand :String? = "",
    val _id : String? =""
) : Parcelable

@Parcelize
data class SubManufacturingMill(
    var _id : String ="",
    var code : String ="",
    var name : String =""
) : Parcelable

@Parcelize
data class SubManufacturingPlan(
    var _id : String ="",
    var code : String ="",
    var salesOrders : List<SubSaleOrder>? = listOf()
) : Parcelable
@Parcelize
data class SubSaleOrder(
    var _id : String? ="",
    var code : String? ="",
): Parcelable
@Parcelize
data class SubApproverModel(
    var approvedTime : String = "",
    var approver : SubUserBasicModel? = SubUserBasicModel(),

): Parcelable

@Parcelize
data class SubGoodModel(
    var _id: String? = "",
    var baseUnit: String? = "",
    var code: String? = "",
    var name: String? = "",
    var materials : List<SubMaterialModel>? = listOf()
) : Parcelable

@Parcelize
data class SubMaterialModel(
    var good : GoodModel? = GoodModel(),
    var quantity : Int? = 0
) : Parcelable{
    override fun toString(): String {
      return good?.name + " - "+good?.code ?: ""
    }
}

@Parcelize
data class SubQualityControlStaff(
    var status : Int = 0,
    var content : String? = "",
    var time : String? = "",
    var staff : SubUserBasicModel = SubUserBasicModel()
) : Parcelable