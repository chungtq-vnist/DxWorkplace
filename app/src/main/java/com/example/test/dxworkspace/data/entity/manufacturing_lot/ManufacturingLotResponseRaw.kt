package com.example.test.dxworkspace.data.entity.manufacturing_lot

import android.os.Parcelable
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubManufacturingWorkInMill
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import kotlinx.android.parcel.Parcelize

data class ManufacturingLotResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: ManufacturingLotResponseWrap? = ManufacturingLotResponseWrap()
)
data class ManufacturingLotResponseWrap(
    val lots: ManufacturingLotResponse? = ManufacturingLotResponse()
)

data class ManufacturingLotResponse(
    val docs: List<ManufacturingLotModel>? = listOf()
)

data class ManufacturingLotModel(
    var _id : String ="",
    var code : String = "",
    var type : String? ="",
    var description : String? = "",
    var createdAt : String = "",
    var productType : Int? = 0 ,
    var status : Int = 0 , // 1. Chưa lên đơn nhập kho 2. Đã lên đơn nhập kho 3. Đã nhập kho
    var expirationDate : String? = "",
    var creator : SubUserBasicModel? =SubUserBasicModel(),
    var bills : List<String>? = listOf(),
    var passedQualityControl : Int? = 0,
    var originalQuantity : Int? = 0 ,
    var quantity : Int? = 0,
    var good : SubGoodsInLot? = SubGoodsInLot()
)

@Parcelize
data class SubGoodsInLot(
    var _id: String = "",
    var code : String ="",
    var name  : String ="",
    var baseUnit : String? = "",


) : Parcelable