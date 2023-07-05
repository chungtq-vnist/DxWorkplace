package com.example.test.dxworkspace.presentation.model.menu

data class RequestUpdateInfoLot (
    var description : String? ="",
    var expirationDate : String? ="",
    var originalQuantity : Int? = 0
        )