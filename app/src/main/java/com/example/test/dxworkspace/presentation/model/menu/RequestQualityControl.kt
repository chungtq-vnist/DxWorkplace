package com.example.test.dxworkspace.presentation.model.menu

data class RequestQualityControl(
    var qualityControlStaff: RequestQualityControlStaff = RequestQualityControlStaff()
)

data class RequestQualityControlStaff(
    var staff: String = "",
    var status: String = "",
    var content: String = ""
)
