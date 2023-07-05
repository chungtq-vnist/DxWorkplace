package com.example.test.dxworkspace.presentation.model.menu

data class ParamGetProductRequest(
    var requestType: Int = 0,
    var requestFrom: String = "",
    var from: String = "",
    var to: String = ""
)