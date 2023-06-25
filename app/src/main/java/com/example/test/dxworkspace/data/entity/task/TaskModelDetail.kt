package com.example.test.dxworkspace.data.entity.task

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class TaskModelDetail(
    var _id : String ="",
    var code : String = "",
    var numberOfDaysTaken : Int = 0,
    var organizationalUnit : OrganizationalUnit? = null,
    var creator : UserProfileResponse = UserProfileResponse(),
    var name : String ="",
    var description : String ="",
    var startDate : String = "",
    var endDate : String = "",
    var priority : Int = 3, // 1: Thấp, 2: Trung Bình, 3: Tiêu chuẩn, 4: Cao, 5: Khẩn cấp
    var status : String ="", // có 5 trạng thái công việc: Đang thực hiện, Chờ phê duyệt, Đã hoàn thành, Tạm hoãn, Bị hủy
    var responsibleEmployees : List<UserProfileResponse>? = emptyList(), //người thưcj hiện
    var accountableEmployees : List<UserProfileResponse>? = emptyList(), // người phê duyệt
    var consultedEmployees : List<UserProfileResponse>? = emptyList(), // người tư vấn
    var informedEmployees : List<UserProfileResponse>? = emptyList(), // người quan sát
    var confirmedByEmployees : List<UserProfileResponse>? = emptyList(), // những người đã xác nhận công việc
    var progress : Int = 0, // % hoan thanh cong viec
    var point : Int = -1,
    var timesheetLogs : List<TimeSheetLog> = emptyList(),
    var taskActions : List<TaskAction> = emptyList(), // cac hoat dong cua task
    var estimateNormalTime : Int? = 0,
    var estimateOptimisticTime : Long? = 0L,
    var estimateNormalCost :  Long? = 0L,
    var actualCost :  Long? = 0L,
    var actualStartDate : String? = "",
    var actualEndDate : String? = ""

)

data class TaskDetailResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : TaskModelDetail = TaskModelDetail()
)

data class TimeSheetResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : List<TimeSheetLog>? = listOf()
)

data class CreateTaskActionResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : List<TaskAction>? = listOf()
)