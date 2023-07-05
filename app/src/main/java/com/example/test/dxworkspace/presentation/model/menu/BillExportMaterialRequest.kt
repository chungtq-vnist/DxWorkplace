package com.example.test.dxworkspace.presentation.model.menu

data class BillExportMaterialRequest (
    var fromStock: String ="",
    var group: String ="",
    var code: String ="",
    var type: String ="",
    var status: String ="",
    var creator: String ="",
    var approvers: MutableList<Approver> = mutableListOf(),
    var responsibles: MutableList<String> = mutableListOf(),
    var accountables: MutableList<String> = mutableListOf(),
    var qualityControlStaffs: MutableList<QualityControlStaff> = mutableListOf(),
    var receiver: Receiver = Receiver(),
    var description: String ="",
    var goods: MutableList<Good> = mutableListOf(),
    var manufacturingMill: String ="",
    var manufacturingCommand: String = ""
) {
}

data class Approver (
    val approver: String ="",
//    val approvedTime: Any? = null
)

data class Good (
    val good: String ="",
    val quantity: Int=0,
    val code : String ="",
    val lots : List<SubLot>? = null,
)

data class SubLot(
    var lot : String ="",
    var quantity : Int = 0
)

data class QualityControlStaff (
    val staff: String ="",
    val status: Int =0
)

data class Receiver (
    val name: String ="",
    val phone: String ="",
    val email: String ="",
    val address: String =""
)

data class RequestApproveCommand(
    var approver : String ="",
    var status : Int = 0
)

data class RequestUpdateLot(
    val status : Int = 0
)