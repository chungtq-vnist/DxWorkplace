package com.example.test.dxworkspace.data.entity.organization_unit

import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit

data class OrganizationUnitResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: OrganizationUnitResponseWrap? = OrganizationUnitResponseWrap()
)

data class OrganizationUnitResponseWrap(

    val list: List<OrganizationUnit>? = listOf()
)