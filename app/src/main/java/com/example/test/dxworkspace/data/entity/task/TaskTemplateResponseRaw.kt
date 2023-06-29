package com.example.test.dxworkspace.data.entity.task

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class TaskTemplateResponseRaw (
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content :TaskTemplateResponseWrap? = TaskTemplateResponseWrap()
        )

data class  TaskTemplateResponseWrap(
    val docs : List<TaskTemplateResponse>?= listOf()
)
data class TaskTemplateResponse(
    val collaboratedWithOrganizationalUnits :List<CollaboratedWithOrganizationalUnits>? = listOf(),
    var accountableEmployees : List<UserProfileResponse>? = emptyList(),
    var consultedEmployees : List<UserProfileResponse>? = emptyList(), // người tư vấn
    var informedEmployees : List<UserProfileResponse>? = emptyList(),
    var responsibleEmployees : List<UserProfileResponse>? = emptyList(), //người thưcj hiện
    var creator : UserProfileResponse = UserProfileResponse(),
    var description : String? ="",
    var formula : String? ="",
    var name : String ="",
    var organizationalUnit : OrganizationalUnit? = null,
    var priority : Int = 3, // 1: Thấp, 2: Trung Bình, 3: Tiêu chuẩn, 4: Cao, 5: Khẩn cấp
    var taskActions : List<TaskAction> = emptyList(), // cac hoat dong cua task
    var status :Boolean? = false,
    var _id : String ="",

    )

data class CollaboratedWithOrganizationalUnits(
    val _id : String = "",
    var name : String = "",
    var description : String = ""
)

// chỉ áp dụng template khi tạo task mới với điều kiện : trùng organization Id và trùng creator