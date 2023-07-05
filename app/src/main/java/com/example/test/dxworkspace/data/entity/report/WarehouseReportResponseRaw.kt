package com.example.test.dxworkspace.data.entity.report

data class WarehouseReportResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content : List<WarehouseReportModel>? = listOf()
    )

data class WarehouseReportModel(

    val productBeginningValue : Int = 0,
    val materialBeginningValue : Int = 0,
    val equipmentBeginningValue : Int = 0,
    val wasteBeginningValue : Int = 0,
    val productImportValue :Int =   0,
    val materialImportValue:Int = 0,
    val equipmentImportValue:Int = 0,
    val wasteImportValue:Int = 0,
    val productExportValue:Int =0,
    val materialExportValue :Int = 0,
    val equipmentExportValue :Int = 0,
    val wasteExportValue :Int = 0,
    val productEndingValue :Int =0,
    val materialEndingValue :Int = 0,
    val equipmentEndingValue :Int = 0,
    val wasteEndingValue :Int = 0,
    val listProduct : List<ItemInWarehouseReport>? = listOf() ,
    val listMaterial : List<ItemInWarehouseReport>? = listOf() ,
    val listEquipment : List<ItemInWarehouseReport>? = listOf() ,
    val listWaste : List<ItemInWarehouseReport>? = listOf() ,

)
data class ItemInWarehouseReport(
    val good : GoodInWarehouseReport = GoodInWarehouseReport(),
    val beginningQuantity : Int = 0,
    val endingQuantity : Int = 0,
    val importQuantity : Int = 0,
    val exportQuantity : Int = 0
)

data class GoodInWarehouseReport(
    val _id : String ="",
    val code : String ="",
    val name : String ="",
    val type : String ="",
    val baseUnit : String? = "",
    val pricePerBaseUnit :  Double? = 0.0,

)