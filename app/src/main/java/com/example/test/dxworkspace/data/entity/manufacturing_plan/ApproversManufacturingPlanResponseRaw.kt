package com.example.test.dxworkspace.data.entity.manufacturing_plan

import com.example.test.dxworkspace.data.entity.manufacturing_work.UserRoleInOrganizationUnit

data class ApproversManufacturingPlanResponseRaw(
    val success: Boolean = false,
    val messages: List<String> = emptyList(),
    val content: ApproversManufacturingPlanWrap? = ApproversManufacturingPlanWrap()
)

data class ApproversManufacturingPlanWrap(
    val users: List<UserRoleInOrganizationUnit>? = listOf()
)