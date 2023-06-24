package com.example.test.dxworkspace.data.entity.dashboard_manufacturing

data class QualityGoodsCompare(
    var numberOfProducts: Int? = 0,
    var numberOfWaste: Int? = 0,
    var numberOfProductsCompare: Int? = 0,
    var numberOfWasteCompare: Int? = 0,
    var goods : Goods? = Goods(),
    var qualityPercent : Double? = null,
    var qualityPercentConpare : Double? = null,


)

data class QualityGoods(
    val numberOfProducts: Int? = 0,
    val numberOfWaste: Int? = 0,
    val _id: String? = "",
    val good :Goods? = null,
    val manufacturingWorks : String? ="",
    val createdAt : String? = "",
    val updatedAt : String? = ""
)

data class Goods(
    val _id : String? =  "",
    val name : String? = "",
    val code : String? = "",
    val pricePerBaseUnit : Double? = 0.0

)
data class DashboardManufacturingQualityGoodsContent(
    val data: List<QualityGoods>? = listOf(),
    val dataCompare: List<QualityGoods>? = listOf()
)

data class DashboardManufacturingQualityGoodsRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: DashboardManufacturingQualityGoodsContent? = DashboardManufacturingQualityGoodsContent()
)