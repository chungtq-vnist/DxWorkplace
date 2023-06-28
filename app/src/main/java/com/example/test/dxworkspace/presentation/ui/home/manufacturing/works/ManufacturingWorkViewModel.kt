package com.example.test.dxworkspace.presentation.ui.home.manufacturing.works

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.role.RoleEntity
import com.example.test.dxworkspace.data.local.datasource.ManufacturingLotLocalSource
import com.example.test.dxworkspace.data.local.datasource.ManufacturingWorkLocalSource
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllOrganizationUnitRemoteUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetAllManufacturingWorkRemoteUsecase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetManufacturingWorkByIdUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetManufacturingWorkUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class ManufacturingWorkViewModel @Inject constructor(
    private val getManufacturingWorkUseCase : GetManufacturingWorkUseCase,
    private val manufacturingWorkLocalSource: ManufacturingWorkLocalSource,
    private val getAllManufacturingWorkRemoteUsecase: GetAllManufacturingWorkRemoteUsecase,
    private val configRepository: ConfigRepository,
    private val getAllOrganizationUnitRemoteUseCase: GetAllOrganizationUnitRemoteUseCase,
    private val authRepository: AuthRepository,
    private val getManufacturingWorkByIdUseCase: GetManufacturingWorkByIdUseCase,
) :BaseViewModel() {
    val listWorks = MutableLiveData<List<ManufacturingWorkModel>>()

    val workDetail = MutableLiveData<ManufacturingWorkDetailModel>()
    fun getManufacturingWorks(){
        getAllManufacturingWorkRemoteUsecase(""){
            it.either({},{
                listWorks.value = it
            })

        }
    }

    fun getWorkById(id : String ) {
       getManufacturingWorkByIdUseCase(id){
           showLoading(true)
           it.either({
               showLoading(false)
           },{
               showLoading(false)
               workDetail.value = it
           })
       }
    }

}