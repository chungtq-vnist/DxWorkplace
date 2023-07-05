package com.example.test.dxworkspace.data.entity.manufacturing_mill

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class ManufacturingMillResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: ManufacturingMillResponseWrap? = ManufacturingMillResponseWrap()
)

data class ManufacturingMillResponseWrap(
    val manufacturingMills: ManufacturingMillResponse? = ManufacturingMillResponse()
)

data class ManufacturingMillResponse(
    val docs: List<ManufacturingMillModel>? = listOf()
)

data class ManufacturingMillModel(
    var _id : String ="",
    var code : String = "",
    var name : String ="",
    var description : String? = "",
    var createdAt : String = "",
    var status : Int = 0,
    var manufacturingWorks :SubManufacturingWorkInMill ? = null,
    var teamLeader : SubUserBasicModel? = null
){
    override fun toString(): String {
        return name
    }
}

data class SubManufacturingWorkInMill(
    var _id : String ="",
    var name : String =""
)

@Parcelize
data class SubUserBasicModel(
    var _id : String = "",
    var name : String ="",
    var email : String = "",
    var avatar : String? = ""
) : Parcelable




data class ManufacturingMillByIdResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: ManufacturingMillByIdResponse? = ManufacturingMillByIdResponse()
)

data class ManufacturingMillByIdResponse(
    val manufacturingMill : ManufacturingMillModel? = ManufacturingMillModel()
)