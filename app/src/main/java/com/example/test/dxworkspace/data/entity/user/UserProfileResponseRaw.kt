package com.example.test.dxworkspace.data.entity.user

import com.example.test.dxworkspace.data.entity.login.CompanyModel
import com.example.test.dxworkspace.data.entity.login.LoginResponseContent
import com.example.test.dxworkspace.data.entity.login.RoleResponseRaw
import com.google.gson.annotations.SerializedName

data class UserProfileResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: UserProfileResponse = UserProfileResponse()
)

data class UserProfileResponse(
    @SerializedName("_id")
    val id : String ="",
    val name : String ="",
    val email :String ="",
    val avatar : String = "",
    val roles : MutableList<RoleResponseRaw> = mutableListOf(),
){
    override fun toString(): String {
        return name
    }
}