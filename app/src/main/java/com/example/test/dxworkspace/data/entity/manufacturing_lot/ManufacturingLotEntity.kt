package com.example.test.dxworkspace.data.entity.manufacturing_lot

import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ManufacturingLotEntity : RealmObject {
    @PrimaryKey
    var _id : String =""
    var code : String = ""
    var type : String? =""
    var description : String? = ""
    var createdAt : String = ""
    var productType : Int? = 0
    var status : Int = 0
    var expirationDate : String? = ""
    var creator : String? = ""
    var bills : String? = ""
    var passedQualityControl : Int? = 0
    var originalQuantity : Int? = 0
    var quantity : Int? = 0
    var good : String? = ""
}