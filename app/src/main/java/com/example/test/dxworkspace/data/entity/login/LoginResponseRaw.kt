package com.example.test.dxworkspace.data.entity.login

import android.os.Parcelable
import com.example.test.dxworkspace.data.entity.role.RoleEntity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LoginResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : LoginResponseContent = LoginResponseContent()
)

data class LoginResponseContent(
    val token :String ="",
    val password2Exists : Boolean = false ,
    val portal : String ="",
    val user : UserResponseRaw = UserResponseRaw(),

)

data class UserResponseRaw(
    @SerializedName("_id")
    var id : String ="",
    var name : String ="",
    var email :String ="",
    var avatar : String = "",
    var roles : MutableList<RoleResponseRaw> = mutableListOf(),
    var company : CompanyModel = CompanyModel(),

)
@Parcelize
data class RoleResponseRaw(
    val roleId : RoleResponse = RoleResponse()
) : Parcelable

@Parcelize
data class RoleResponse(
    @SerializedName("_id")
    val id : String = "",
    val name : String ="",
    val parents : List<String> = emptyList(),

) : Parcelable

