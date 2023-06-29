package com.example.test.dxworkspace.data.entity.task

data class RequestCreateTask(
    var name: String ="",
    var description: String? ="",
    var quillDescriptionDefault: String? ="",
    var startDate: String ="",
    var endDate: String ="",
    var startTime: String ="",
    var endTime: String ="",
    var priority: Int = 0,
    var responsibleEmployees: List<String>? = listOf(),
    var accountableEmployees: List<String>? = listOf(),
    var consultedEmployees: List<String>? = listOf(),
    var informedEmployees: List<String>? = listOf(),
    var creator: String ="",
    var organizationalUnit: String ="",
    var collaboratedWithOrganizationalUnits: MutableList<CollaboratedWithOrganizationalUnitRequest>? = mutableListOf(),
    var taskTemplate: String? = "",
    var parent: String? = "",
    var taskProject: String? = "",
    var tags: List<String>? = listOf(),
    var taskOutputs: List<TaskOutputRequest>? = listOf(),
)

data class TaskOutputRequest(
    val title: String?,
    val description: String?,
    val type: Long,
)

data class CollaboratedWithOrganizationalUnitRequest(
    val organizationalUnit: String,
    val isAssigned: Boolean = false
)