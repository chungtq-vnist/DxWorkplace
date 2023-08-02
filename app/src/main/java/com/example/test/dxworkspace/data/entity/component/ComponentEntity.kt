package com.example.test.dxworkspace.data.entity.component

import com.example.test.dxworkspace.data.entity.link.LinkEntity
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ComponentEntity : RealmObject {
    @PrimaryKey
    var _id : String =""
    var name : String = ""
    var description : String = ""
    var deleteSoft : Boolean = false
    var links : String = ""
//    var attributes : List<String> = listOf()
    var createdAt : String  = ""
    var updatedAt : String = ""
}

