package com.example.test.dxworkspace.data.entity.role

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RoleEntity : RealmObject {
    @PrimaryKey
    var id : String =""
    var name : String =""
//    var parents : MutableList<String> = mutableListOf()
    var createdAt : String =""
    var updatedAt : String =""

}