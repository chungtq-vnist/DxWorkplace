package com.example.test.dxworkspace.data.entity.manufacturing_command

data class StockResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content :List<StockModel>? = listOf()
    )

data class StockModel(
    var _id : String ="",
    var code : String ="",
    var name : String ="",

) {
    override fun toString(): String {
        return name
    }
}