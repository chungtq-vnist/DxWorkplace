package com.example.test.dxworkspace.data.entity.manufacturing_plan

import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubApproverModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubSaleOrder
import com.example.test.dxworkspace.data.entity.manufacturing_lot.SubGoodsInLot
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect

data class ManufacturingPlanDetailResponseRaw (
     val success: Boolean = false,
     val messages: List<String> = emptyList(),
     val content :ManufacturingPlanDetailResponse? = ManufacturingPlanDetailResponse()
         )

data class ManufacturingPlanDetailResponse(
    val manufacturingPlan :ManufacturingPlanDetailModel? = ManufacturingPlanDetailModel()
)

data class ManufacturingPlanDetailModel(
    val code: String = "",
    val approvers: List<SubApproverModel>? = listOf(),
    var creator : SubUserBasicModel? = SubUserBasicModel(),
    val startDate: String = "",
    val endDate: String = "",
    val description: String? ="",
    val createdAt: String ="",
    val _id : String ="",
    val status : Int = 0, // Trạng thái kế hoạch: 1. Đang chờ duyệt || 2. Đã phê duyệt || 3. Đang thực hiện || 4. Đã hoàn thành || 5. Đã hủy
    val goods : List<SubGoodInPlan>?= listOf(),
    val manufacturingCommands : List<ManufacturingCommandModel>? = listOf(),
    val manufacturingWorks :List<ManufacturingWorkModel>? = listOf(),
    val salesOrders : List<SubSaleOrder>? = listOf()

)
data class SubGoodInPlan(
    val good : SubGoodsInLot? =SubGoodsInLot(),
    val quantity : Int = 0,
    val _id : String =""
)