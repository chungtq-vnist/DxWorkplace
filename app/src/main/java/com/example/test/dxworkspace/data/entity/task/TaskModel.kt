package com.example.test.dxworkspace.data.entity.task

import com.example.test.dxworkspace.data.entity.login.LoginResponseContent
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

data class TaskModel(
    var _id : String ="",
    var code : String = "",
    var numberOfDaysTaken : Int = 0,
//    var organizationalUnit : OrganizationalUnit? = null,
    var creator : UserProfileResponse = UserProfileResponse(),
    var name : String ="",
    var description : String ="",
    var startDate : String = "",
    var endDate : String = "",
    var priority : Int = 3, // 1: Thấp, 2: Trung Bình, 3: Tiêu chuẩn, 4: Cao, 5: Khẩn cấp
    var status : String ="", // có 5 trạng thái công việc: Đang thực hiện, Chờ phê duyệt, Đã hoàn thành, Tạm hoãn, Bị hủy
    var responsibleEmployees : List<UserProfileResponse> = emptyList(), //người thưcj hiện
    var accountableEmployees : List<String> = emptyList(), // người phê duyệt
    var consultedEmployees : List<String> = emptyList(), // người tư vấn
    var informedEmployees : List<String> = emptyList(), // người quan sát
    var confirmedByEmployees : List<String> = emptyList(), // những người đã xác nhận công việc
    var progress : Int = 0 , // % hoan thanh cong viec
    var point : Int = -1 ,
//    var timesheetLogs : List<TimeSheetLog> = emptyList(),
//    var taskActions : List<TaskAction> = emptyList(), // cac hoat dong cua task
    var estimateNormalTime : Int? = 0,
    var estimateOptimisticTime : Long? = 0L,
    var estimateNormalCost :  Long? = 0L,
    var actualCost :  Long? = 0L,
    var actualStartDate : String? = "",
    var actualEndDate : String? = ""



)

data class OrganizationalUnit(
    var name : String? = "",
    var _id : String? = "" ,
    var description: String? = "",
    var parent : String? = null ,
    var managers : List<String>? = emptyList(),
    var deputyManagers : List<String>? = emptyList(),
    var employees : List<String>? = emptyList(),
    var createdAt: String? = "",
    var updatedAt: String? = ""
)

data class TimeSheetLog(
    var autoStopped: Int? = 0,  // 1: Tắt bấm giờ bằng tay, 2: Tắt bấm giờ tự động với thời gian hẹn trc, 3: add log timer
    var acceptLog: Boolean? = false,

    var _id: String = "",

    var startedAt: String = "" ,
    var creator: UserProfileResponse? = null,
    var description: String? = "",
    var duration: Int? = 0  ,
    var stoppedAt: String? = "",
)

data class TaskAction(
    var _id : String ="",
    var creator : UserProfileResponse? = null ,
    var mandatory : Boolean = false ,
    var rating : Int? = -1 ,
    var description: String? = "",
    var createdAt : String? = "",
    var updatedAt : String? = "",
    var actionImportanceLevel : Int? = 0 ,
    var comments: List<Comment>? = emptyList(),
    var timesheetLogs : List<TimeSheetLog>? = emptyList(),

)

data class Comment(
    var creator : UserProfileResponse? = null,
    var description: String? = "",
    var createdAt: String? = ""
)

data class TaskResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : TaskResponseContent = TaskResponseContent()
)

data class TaskResponseContent(
    var tasks : List<TaskModel>? = emptyList(),
    var totalPage : Int = 0,
    var totalCount : Int = 0,
)

object TaskType{
    const val RESPONSIBLE = "responsible"
    const val ACCOUNTABLE= "accountable"
    const val CONSULTED= "consulted"
    const val INFORMED ="informed"
    const val CREATOR = "creator"
}
