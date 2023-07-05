package com.example.test.dxworkspace.data.entity.bill

import com.example.test.dxworkspace.data.entity.manufacturing_command.SubGoodModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

// data thong tin phei xuat nguyen vat lieu cua lenh san xuat
 data class BillByCommandResponseRaw(
     val success : Boolean = false,
     val messages : List<String> = emptyList(),
     val content :List<BillByCommandModel>? = listOf()
 )
data class BillByCommandModel(
    val receiver : SubUserBasicModel? = SubUserBasicModel(),
    val fromStock :SubStockModel? = SubStockModel(),
    val _id : String? = "",
    val type : String? ="",
    val group : String? ="",
    val goods :List<SubGoodsBill>? = listOf(),
    val lot : String,
    val status : String = ""
)


data class SubGoodsBill(
    val good : SubGoodModel? = SubGoodModel(),
    val quantity : Int? = 0,
    val realQuantity : Int? = 0
)

data class SubStockModel(
    val _id: String = "",
    val code: String ="",
    val name: String ="",
)