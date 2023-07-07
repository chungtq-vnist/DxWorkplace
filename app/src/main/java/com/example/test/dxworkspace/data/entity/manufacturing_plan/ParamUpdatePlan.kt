package com.example.test.dxworkspace.data.entity.manufacturing_plan

data class ParamUpdatePlan(
    var approvers: ParamApprover? = null,
    var status: Int? = null
)

data class ParamApprover(
    var approver: String = ""
)