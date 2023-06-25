package com.example.test.dxworkspace.presentation.model.menu

data class CompareModel(
    val now : Double = 0.0,
    val compare : Double =0.0,
    val type : String ="" ,// money - value
    val title : String ="",
    val iconId : Int =0
)