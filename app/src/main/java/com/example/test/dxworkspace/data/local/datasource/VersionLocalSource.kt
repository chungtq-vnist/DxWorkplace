package com.example.test.dxworkspace.data.local.datasource

import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.version.VersionEntity
import com.example.test.dxworkspace.data.local.database.RealmManager
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.Versioned
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import javax.inject.Inject

class VersionLocalSource @Inject constructor() {
    lateinit var realm: Realm
    fun getDatabase(dbName: String) {
        realm = RealmManager.get(dbName)
    }

    suspend fun getVersions(dbName: String): List<VersionEntity> {
        getDatabase(dbName)
        val version = realm.query<VersionEntity>().find().toList() ?: emptyList()
        return version
    }

    suspend fun saveVersion(c: VersionEntity, dbName: String) {
        getDatabase(dbName)
        realm.write {
            copyToRealm(c, UpdatePolicy.ALL)
        }
    }

    suspend fun saveVersions(c: List<VersionEntity>, dbName: String) {
        getDatabase(dbName)
        realm.write {
            c.forEach {
                copyToRealm(it, UpdatePolicy.ALL)
            }
        }

    }

    suspend fun getVersionByKey(key: String, dbName: String): VersionEntity {
        getDatabase(dbName)
        val v = realm.query<VersionEntity>("versionKey == $key").first().find()
            ?: VersionEntity().apply {
                versionKey = key
                value = 0
            }
        return v
    }


}