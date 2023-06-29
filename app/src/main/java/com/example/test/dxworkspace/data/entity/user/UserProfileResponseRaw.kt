package com.example.test.dxworkspace.data.entity.user

import android.os.Parcel
import android.os.Parcelable
import com.example.test.dxworkspace.data.entity.login.CompanyModel
import com.example.test.dxworkspace.data.entity.login.LoginResponseContent
import com.example.test.dxworkspace.data.entity.login.RoleResponseRaw
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class UserProfileResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: UserProfileResponse = UserProfileResponse()
)

@Parcelize
data class UserProfileResponse(
    @SerializedName("_id")
    val id : String ="",
    val name : String ="",
    val email :String ="",
    val avatar : String = "",
    val roles : MutableList<RoleResponseRaw> = mutableListOf(),
) : Parcelable {
    override fun toString(): String {
        return name
    }
}