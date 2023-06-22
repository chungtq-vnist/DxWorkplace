package com.example.test.dxworkspace.data.entity.user

import com.example.test.dxworkspace.data.entity.role.RoleEntity
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UserEntity : RealmObject {
    @PrimaryKey
    var id : ObjectId = ObjectId.create()
    var name : String =""
    var email  : String = ""
    var company : String =""
    var portal : String = ""
    var password2Exists : Boolean = false
    var roles : RealmList<RoleEntity> = realmListOf()
}