package com.example.test.dxworkspace.data.entity.notify

data class PaginateNotificationsResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content :PaginateNotificationsResponse? = PaginateNotificationsResponse()
)

data class PaginateNotificationsResponse(
    val docs : List<NotificationModel>? = listOf(),
    val totalDocs: Int = 0,
    val limit: Int =0,
    val totalPages: Int =0,
    val page: Int =0,
    val pagingCounter: Int =0,
    val hasPrevPage: Boolean = true,
    val hasNextPage: Boolean = true,
    val prevPage: Int =0,
    val nextPage: Int  =0,
)

data class NotificationModel(
    val associatedDataObject: AssociatedDataObject? = AssociatedDataObject(),
    var readed: Boolean = false,
    val title: String ="",
    val level: String ="",
    val content: String ="",
    val sender: String ="",
    val user: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val id: String = ""
)

data class AssociatedDataObject(
    val dataType: Int = 1, //Task: 1, Asset: 2, KPI:3 , 5 : Đề nghị san xuat ( sản phẩm ) , 6 : Quy trình công việc
    val description: String =""
)