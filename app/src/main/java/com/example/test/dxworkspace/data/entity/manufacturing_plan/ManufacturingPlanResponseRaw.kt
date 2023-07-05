package com.example.test.dxworkspace.data.entity.manufacturing_plan

import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubApproverModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubGoodModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import java.io.Serializable

data class ManufacturingPlanResponseRaw (
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content :ManufacturingPlanResponseWrap? = ManufacturingPlanResponseWrap()
        )
data class ManufacturingPlanResponseWrap(
    val manufacturingPlans :ManufacturingPlanResponse? = ManufacturingPlanResponse()
)

data class ManufacturingPlanResponse(
    val docs : List<ManufacturingPlanModel>? = listOf()
)

data class ManufacturingPlanModel(
    val code: String = "",
    val approvers: List<SubApproverModel>? = listOf(),
    var creator : SubUserBasicModel? = SubUserBasicModel(),
    val startDate: String = "",
    val endDate: String = "",
    val description: String? ="",
    val createdAt: String ="",
    val _id : String ="",
    val status : Int = 0, // Trạng thái kế hoạch: 1. Đang chờ duyệt || 2. Đã phê duyệt || 3. Đang thực hiện || 4. Đã hoàn thành || 5. Đã hủy
    val manufacturingWorks: List<String> = listOf(),
    var goods : List<SubGoodPlan>? = listOf() ,
    var saleOrders : List<String>? = listOf(),
    var manufacturingCommands : List<ManufacturingCommandModel>? = listOf(),

    ) : Serializable

data class SubGoodPlan(
    val good : String ="",
    val quantity : Int = 0,
    val _id : String =""
)

data class SubManufacturingCommand(
    val _id : String
)