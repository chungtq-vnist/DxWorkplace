package com.example.test.dxworkspace.data.entity.good

import android.os.Parcelable
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandDetailWrap
import kotlinx.android.parcel.Parcelize

data class InventoryGoodResponseRaw (
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: List<InventoryGoodWrap>? = listOf()

)

@Parcelize
data class InventoryGoodWrap(
    val good : GoodModel,
    val inventory :Int? = 0
) : Parcelable