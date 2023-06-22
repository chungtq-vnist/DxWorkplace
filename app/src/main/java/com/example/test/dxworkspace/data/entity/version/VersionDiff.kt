package com.example.test.dxworkspace.data.entity.version

data class VersionDiff(
    val versionDB: Int = 0,
    var versionRemote: Int = 0,
    val versionKey: String = "",
)

data class VersionDiffResponseRaw(
    val success : Boolean = false,
    val messages : List<String> = emptyList(),
    val content : List<String> = emptyList(),
)