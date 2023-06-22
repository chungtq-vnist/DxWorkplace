package com.example.test.dxworkspace.data.entity.login

import com.google.gson.annotations.SerializedName

data class CompanyModel(
    val active : Boolean = true,
    @SerializedName("_id")
    val id : String ="",
    val name : String ="",
    val shortName : String ="",
)
