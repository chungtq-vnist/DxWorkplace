package com.example.test.dxworkspace.data.local.datasource

import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandEntity
import com.example.test.dxworkspace.data.entity.role.RoleEntity
import com.example.test.dxworkspace.data.local.database.RealmManager
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import javax.inject.Inject

class AuthLocalSource @Inject constructor() {
    lateinit var realm: Realm
    fun getDatabase(dbName: String) {
        realm = RealmManager.get(dbName)
    }

    suspend fun getAllLinksLocal(dbName: String): List<LinkEntity> {
        getDatabase(dbName)
        val list = realm.query<LinkEntity>().find().toList()
        return list
    }

    suspend fun saveLink(c: LinkEntity, dbName: String) {
        getDatabase(dbName)
        realm.write {
            copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    suspend fun saveLinks(list: List<LinkEntity>, dbName: String) {
        getDatabase(dbName)
        realm.write {
            for (c in list)
                copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    suspend fun savesAndDeleteRoles(listComponent: List<RoleEntity>, dbName: String) {
        getDatabase(dbName)
        realm.write {
            val listNow = query<RoleEntity>().find()
            delete(listNow)
            for (c in listComponent)
                copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    fun getAllRoleDb(dbName: String): List<RoleEntity> {
        getDatabase(dbName)
        val list = realm.query<RoleEntity>().find().toList()
        return list
    }

}