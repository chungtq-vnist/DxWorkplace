package com.example.test.dxworkspace.data.entity.manufacturing_mill

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ManufacturingMillEntity : RealmObject {
    @PrimaryKey
    var _id : String = ""
    var code : String = ""
    var name : String =""
    var description : String? = ""
    var createdAt : String = ""
    var status : Int = 0
//    var manufacturingWorks :SubManufacturingWorkInMill ? = null,
    var manufacturingWorks : String? = ""
//    var teamLeader : SubUserBasicModel? = null
    var teamLeader : String? = ""
}