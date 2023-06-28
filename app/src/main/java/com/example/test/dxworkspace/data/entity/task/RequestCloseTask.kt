package com.example.test.dxworkspace.data.entity.task

import com.example.test.dxworkspace.data.entity.user.UserProfileResponse

object TaskStatus{
    val finished = "Đã hoàn thành"
    val delayed = "Tạm hoãn"
    val canceled = "Bị hủy"
    val wait_for_approval = "Chờ phê duyệt"

}

data class RequestCloseTask(
    var requestAndApprovalCloseTask : Int? = null, // = 1
    var taskStatus : String? =null,
    //    "wait_for_approval",
//"finished",
//"delayed",
//"canceled",
    var type : String? = null, // cancel_request , approval ,request === open_task_again
    var description : String? =null
)


data class RequestToCloseTaskResponse(
    var accountableUpdatedAt : String? = "",
    var createdAt : String? = "",
    var description : String? = "",
    var requestStatus : Int ? = 0,  // 0: chưa yêu cầu, 1: đang yêu cầu, 2: từ chối, 3: đồng ý
    var responsibleUpdatedAt : String? = "",
    var taskStatus : String? = "",
//    "wait_for_approval",
//"finished",
//"delayed",
//"canceled",
    var requestBy : UserProfileResponse? = null
)