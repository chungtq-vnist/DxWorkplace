package com.example.test.dxworkspace.data.entity.link

import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.component.ComponentResponse
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList

data class LinkResponseRaw (
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : MutableList<LinkResponse> = mutableListOf()
)

data class LinkResponse(
    var _id: String ="",
    var url: String = "",
    var description: String = "",
    var category: String = "",
    var deleteSoft: Boolean = false,
    var components: MutableList<String>? = null,
    //  varal attributes: List<String> = listOf()
    var __v: Int = 1,
    var createdAt: String = "",
    var updatedAt: String = "",
)