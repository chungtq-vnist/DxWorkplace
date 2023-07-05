package com.example.test.dxworkspace.presentation.model.menu

data class RequestCreateLot (
    var code: String ="",
    var type: String ="",
    var originalQuantity: String ="",
    var productType: Int =0,
    var manufacturingCommand: String = "",
    var good: String ="",
    var quantity: String ="",
    var status: Int =0,
    var description: String ="",
    var expirationDate: String ="",
    var creator: String =""
        )