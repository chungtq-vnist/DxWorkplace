package com.example.test.dxworkspace.data.entity.version

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class VersionEntity : RealmObject {
    @PrimaryKey
    var versionKey : String = ""
    var value : Int = 0
//    var user : Int = 0
//    var userRole : Int = 0
//    var component : Int = 0
//    var link : Int = 0
//    var privilege : Int = 0
}