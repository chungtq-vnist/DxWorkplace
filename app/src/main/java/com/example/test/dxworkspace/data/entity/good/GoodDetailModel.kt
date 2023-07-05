package com.example.test.dxworkspace.data.entity.good

import com.example.test.dxworkspace.data.entity.manufacturing_command.SubMaterialModel

data class GoodDetailModel(
    var sourceType: String = "",
    var quantity: Long =0L,
    var _id: String ="",
    var category: String? = "",
    var code: String ="",
    var name: String = "",
    var type: String = "",
    var baseUnit: String = "",
    var materials : List<SubMaterialModel>? = listOf(),
    var manufacturingMills: List<SubManufacturingMillWrap>? = listOf(),
    var description: String ="",
    var pricePerBaseUnit: Long? =0L,
    var salesPriceVariance: Long? =0L,
) {
    override fun toString(): String {
        return "$name-$code"
    }
}

data class SubManufacturingMillWrap(
    val personNumber : Int? = 0,
    val productivity : Int? = 0,
    val manufacturingMill :SubManufacturingMill? = SubManufacturingMill()
)

data class SubManufacturingMill(
    val status: Int? = 1,
    val _id: String ="",
    val code: String ="",
    val name: String = "",
    val manufacturingWorks: String? ="",
    val description: String? ="",
    )


data class GoodManageByRoleResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : GoodManageByRoleResponse? = GoodManageByRoleResponse()
)
data class GoodManageByRoleResponse(
    val goods : List<GoodDetailModel>? = listOf()
)

data class GoodResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : List<GoodDetailModel>? = listOf()
)