package com.example.test.dxworkspace.presentation.ui.home.manufacturing.works

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetManufacturingWorkUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class ManufacturingViewModel @Inject constructor(
    private val getManufacturingWorkUseCase : GetManufacturingWorkUseCase
) :BaseViewModel() {
    val listWorks = MutableLiveData<List<ManufacturingWorkEntity>>()
    fun getManufacturingWorks(){
        getManufacturingWorkUseCase(""){
            it.either({},{
                listWorks.value = it
            })

        }
    }
}