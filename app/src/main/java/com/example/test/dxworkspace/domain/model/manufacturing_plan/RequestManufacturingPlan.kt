package com.example.test.dxworkspace.domain.model.manufacturing_plan

import com.example.test.dxworkspace.data.entity.manufacturing_command.SubGoodModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.SubGoodPlan
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleModel

data class RequestManufacturingPlan(
    var code: String ="",
    var salesOrders: List<String> = listOf(),
    var startDate: String ="",
    var endDate: String ="",
    var description: String ="",
    var goods: List<SubRequestGood> = listOf(),
    var approvers: List<String> = listOf(),
    var creator: String ="",
    var manufacturingCommands: List<SubRequestCommand> = listOf(),
    var listMillSchedules: MutableList<WorkScheduleModel> = mutableListOf(),
    var arrayWorkerSchedules: MutableList<ArrayWorkerSchedule> = mutableListOf()
)

data class SubRequestGood(
    val good : String ,
    val quantity : Int
)

data class SubRequestCommand(
    var code: String ="",
    var goodID: String ="",

    var good: SubGoodPlan = SubGoodPlan(),
    var inventory: Int =0,
    var baseUnit: String ="",
    var quantity: String ="0",
    var approvers: List<String> = listOf(),
    var accountables: List<String> = listOf(),
    var qualityControlStaffs: List<String> = listOf(),
    var description: String ="",
    var manufacturingMill: String ="",
    var startDate: String ="",
    var endDate: String ="",
    var startTurn: Int =0,
    var endTurn: Int =0,
    var responsibles: List<String> = listOf(),
    var completed: Boolean = true
)

data class ArrayWorkerSchedule (
    var index1: Int,
    var index2: Int,
    var month: String,
    var commandCode: String,
    var users: List<String>
)