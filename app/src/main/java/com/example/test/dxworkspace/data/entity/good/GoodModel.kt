package com.example.test.dxworkspace.data.entity.good

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoodModel(
    var _id: String = "",
    var baseUnit: String = "",
    var code: String = "",
    var name: String = ""
) : Parcelable