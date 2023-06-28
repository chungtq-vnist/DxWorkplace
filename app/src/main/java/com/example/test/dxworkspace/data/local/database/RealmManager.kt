package com.example.test.dxworkspace.data.local.database

import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandEntity
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotEntity
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.role.RoleEntity
import com.example.test.dxworkspace.data.entity.user.UserEntity
import com.example.test.dxworkspace.data.entity.version.VersionEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class RealmManager {
    companion object {
        private var instance : Realm? = null

        fun get(dbName : String) : Realm {
            if(instance == null) {
                val config = RealmConfiguration.Builder(
                    schema = setOf(
                        UserEntity::class, ComponentEntity::class, LinkEntity::class,
                        RoleEntity::class,VersionEntity::class,ManufacturingWorkEntity::class,
                        ManufacturingMillEntity::class,ManufacturingLotEntity::class,
                        ManufacturingCommandEntity::class
                    )
                )
                    .name(dbName)
                    .compactOnLaunch()
                    .build()
                instance = Realm.open(config)
            }
            return instance!!
        }

        fun closeDb(){
            if(instance != null) {
                instance = null
            }
        }
    }



}