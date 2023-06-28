package com.example.test.dxworkspace.data.entity.manufacturing_command

import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import io.realm.kotlin.types.RealmObject

class ManufacturingCommandEntity : RealmObject {
    var _id : String = ""
    var code : String = ""
    var status : Int = 0
    var startDate : String =""
    var endDate : String =""
    var createdAt : String =""
    var startTurn : Int = 0
    var endTurn : Int = 0
    var description : String? = ""
//    var accountables : List<SubUserBasicModel>? = listOf() ,
    var accountables : String? = ""
//    var creator : SubUserBasicModel? = SubUserBasicModel(),
    var creator : String? = ""
//    var responsibles : List<SubUserBasicModel>? = listOf(),
    var responsibles : String? =""
//    var approvers : List<SubApproverModel>? = listOf(),
    var approvers : String? = ""
//    var manufacturingMill :SubManufacturingMill? = SubManufacturingMill(),
    var manufacturingMill :String? =""
//    var manufacturingPlan : SubManufacturingPlan? = SubManufacturingPlan(),
    var manufacturingPlan : String? = ""
    var quantity : Int = 0
//    var qualityControlStaffs : List<SubQualityControlStaff>? = listOf(),
    var qualityControlStaffs : String? = ""
//    var good : SubGoodModel = SubGoodModel(),
    var good : String = ""
    var finishedProductQuantity : Int? = null
    var finishedTime : String? = null
    var substandardProductQuantity : Int? = null
}