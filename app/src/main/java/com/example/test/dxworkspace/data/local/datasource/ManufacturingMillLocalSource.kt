package com.example.test.dxworkspace.data.local.datasource

import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.local.database.RealmManager
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import javax.inject.Inject

class ManufacturingMillLocalSource @Inject constructor() {
    lateinit var realm: Realm
    fun getDatabase(dbName: String) {
        realm = RealmManager.get(dbName)
    }

    suspend fun getAll(dbName: String): List<ManufacturingMillEntity> {
        getDatabase(dbName)
        val list = realm.query<ManufacturingMillEntity>().find().toList()
        return list
    }

    suspend fun save(c: ManufacturingMillEntity, dbName: String) {
        getDatabase(dbName)
        realm.write {
            copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    suspend fun saves(listComponent: List<ManufacturingMillEntity>, dbName: String) {
        getDatabase(dbName)
        realm.write {
//            val listNow = query<ManufacturingWorkEntity>().find()
//            delete(listNow)
            for (c in listComponent)
                copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    suspend fun savesAndDelete(listComponent: List<ManufacturingMillEntity>, dbName: String) {
        getDatabase(dbName)
        realm.write {
            val listNow = query<ManufacturingMillEntity>().find()
            delete(listNow)
            for (c in listComponent)
                copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    suspend fun deletes(list : List<String> , dbName: String){
        getDatabase(dbName)
        realm.write {
            val listNow = query<ManufacturingMillEntity>().find()
            for (i in listNow){
                if(list.contains(i._id)) delete(i)
            }
        }
    }


}