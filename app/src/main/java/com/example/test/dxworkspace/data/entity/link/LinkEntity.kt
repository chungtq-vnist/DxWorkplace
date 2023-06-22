package com.example.test.dxworkspace.data.entity.link

import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class LinkEntity : RealmObject {

    @PrimaryKey
    var _id: String =""
    var url: String = ""
    var description: String = ""
    var category: String = ""
    var deleteSoft: Boolean = false
    var components: RealmList<ComponentEntity> = realmListOf()
    //  varal attributes: List<String> = listOf()
    var __v: Int = 1
    var createdAt: String = ""
    var updatedAt: String = ""
}