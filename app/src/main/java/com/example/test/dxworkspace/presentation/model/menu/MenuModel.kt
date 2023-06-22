package com.example.test.dxworkspace.presentation.model.menu

data class MenuModel (
    var id : String ="",
    var category : String ="",
    var level : Int = 0, // 1 : header , 2 : menu normal , 3 : menu child , 4 : line menu
    var desc : String ="",
    var url : String ="",
    var iconStart : Int = 0,
    var iconEnd : Int = 0,

){
    var isVisible : Boolean = false
    var isExpand : Boolean = false
    var isSelected : Boolean = false
}