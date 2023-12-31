package com.example.test.dxworkspace.data.local.datasource

import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandEntity
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotEntity
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.local.database.RealmManager
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import javax.inject.Inject

class ManufacturingLotLocalSource @Inject constructor() {
    lateinit var realm: Realm
    fun getDatabase(dbName: String) {
        realm = RealmManager.get(dbName)
    }

    suspend fun getAll(dbName: String): List<ManufacturingLotEntity> {
        getDatabase(dbName)
        val list = realm.query<ManufacturingLotEntity>().find().toList()
        return list
    }

    suspend fun save(c: ManufacturingLotEntity, dbName: String) {
        getDatabase(dbName)
        realm.write {
            copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    suspend fun saves(listComponent: List<ManufacturingLotEntity>, dbName: String) {
        getDatabase(dbName)
        realm.write {
//            val listNow = query<ManufacturingWorkEntity>().find()
//            delete(listNow)
            for (c in listComponent)
                copyToRealm(c, UpdatePolicy.ALL)
        }
    }
    suspend fun savesAndDelete(listComponent: List<ManufacturingLotEntity>, dbName: String) {
        getDatabase(dbName)
        realm.write {
            val listNow = query<ManufacturingLotEntity>().find()
            delete(listNow)
            for (c in listComponent)
                copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    suspend fun deletes(list : List<String> , dbName: String){
        getDatabase(dbName)
        realm.write {
            val listNow = query<ManufacturingLotEntity>().find()
            for (i in listNow){
                if(list.contains(i._id)) delete(i)
            }
        }
    }


}