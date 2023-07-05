package com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetLotByIdUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetManufacturingLotUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.UpdateInfoLotUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.UpdateLotUseCase
import com.example.test.dxworkspace.presentation.model.menu.RequestUpdateInfoLot
import com.example.test.dxworkspace.presentation.model.menu.RequestUpdateLot
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class ManufacturingLotViewModel @Inject constructor(
    private val getManufacturingLotUseCase: GetManufacturingLotUseCase,
    private val getLotByIdUseCase: GetLotByIdUseCase,
    private val updateLotUseCase: UpdateLotUseCase,
    private val updateInfoLotUseCase: UpdateInfoLotUseCase
) : BaseViewModel() {
    val listLot = MutableLiveData<List<ManufacturingLotModel>>()
    val lotDetail = MutableLiveData<ManufacturingLotDetailModel>()
    val updateStatus = MutableLiveData<Boolean>()
    fun getAllLot(from : String , to:String){
        showLoading(true)
        getManufacturingLotUseCase(Pair(from,to)){
            it.either({
                showLoading(false)
                handleFailure(it)
                listLot.value = listOf()
            },{
                showLoading(false)
                listLot.value = it
            })
        }
    }

    fun getLotById(id : String){
        showLoading(true)
        getLotByIdUseCase(id){
            it.either({
                      showLoading(false)
            },{
                showLoading(false)
                lotDetail.value = it
            })

        }
    }

    fun updateLot(id : String, status : Int){
        showLoading(true)
        updateLotUseCase(Pair(id, RequestUpdateLot(status))){
            it.either({
                updateStatus.value = false
                showLoading(false)
            },{
                updateStatus.value = true
                showLoading(false)
            })
        }
    }

    fun updateInfoLot(id : String, data: RequestUpdateInfoLot){
        showLoading(true)
        updateInfoLotUseCase(Pair(id, data)){
            it.either({
                updateStatus.value = false
                showLoading(false)
            },{
                updateStatus.value = true
                showLoading(false)
            })
        }
    }
}