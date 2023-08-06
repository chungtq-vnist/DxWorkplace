package com.example.test.dxworkspace.data.entity.manufacturing_work

import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ManufacturingWorkEntity : RealmObject {
    @PrimaryKey
    var _id : String =""
    var address : String =""
    var code : String = ""
    var createdAt : String =""
    var description : String? =""
    var name : String =""
    var phoneNumber : String =""
    var status : Int = 1
    var udpatedAt : String =""
    var manufacturingMills : String? = ""
    var manageRoles : String? = ""
    var organizationalUnit : String? = ""
    var turn : Int? = 0
}

fun ManufacturingWorkModel.mapWithName(): ManufacturingWorkSelect {
    return ManufacturingWorkSelect(this._id,this.name)
}