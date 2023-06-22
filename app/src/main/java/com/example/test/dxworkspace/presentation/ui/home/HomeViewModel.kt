package com.example.test.dxworkspace.presentation.ui.home

import android.content.SharedPreferences
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.domain.usecase.version.CheckVersionUseCase
import com.example.test.dxworkspace.domain.usecase.version.HandleVersionUseCase
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences ,
    private val checkVersionUseCase: CheckVersionUseCase,
    private val handleVersionUseCase: HandleVersionUseCase

)  : BaseViewModel() {

    // fromDate va toDate dung de luu rangetime cho dashboard dieu khien san xuat
    var fromDate : String = "01-06-2023"
    var toDate : String = "01-06-2023"

    // luu cac nha may dang duoc chon cho dashboard dieu khien san xuat
    var listWorksSelected = mutableListOf<ManufacturingWorkSelect>()

    init {
        // khoi tao fromDate va toDate cach nhau 1 thang , va toDate lay ngay hien tai
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        toDate = dateFormat.format(calendar.time)

        calendar.add(Calendar.MONTH, -1)
        fromDate = dateFormat.format(calendar.time)
    }

    var checkVersionSuccess = false
    fun checkVersion(handle : (() -> Unit)){
        checkVersionUseCase(""){
            it.either({},{
                handle.invoke()
            })
        }
    }

    fun checkVersion(handleSuccess :((Boolean) -> Unit ) , handleFail : (() -> Unit) ){
         handleVersionUseCase(""){
            it.either({
                handleFail.invoke()
            },{
                handleSuccess.invoke(it)
            })
        }
    }
}