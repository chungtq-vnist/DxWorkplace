package com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
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
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllManufacturingMillRemoteUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllOrganizationUnitRemoteUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetManufacturingMillByIdUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetAllManufacturingWorkRemoteUsecase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetManufacturingWorkByIdUseCase
import com.example.test.dxworkspace.domain.usecase.manufacturing_work.GetManufacturingWorkUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class ManufacturingMillViewModel @Inject constructor(
    private val getAllManufacturingMillRemoteUseCase: GetAllManufacturingMillRemoteUseCase,
    private val configRepository: ConfigRepository,
    private val getAllManufacturingWorkRemoteUsecase: GetAllManufacturingWorkRemoteUsecase,
    private val getAllOrganizationUnitRemoteUseCase: GetAllOrganizationUnitRemoteUseCase,
    private val getManufacturingMillByIdUseCase: GetManufacturingMillByIdUseCase,
) : BaseViewModel() {
    val listMills = MutableLiveData<List<ManufacturingMillModel>>()
    val listWorks = MutableLiveData<List<ManufacturingWorkModel>>()

    val millDetail = MutableLiveData<ManufacturingMillModel>()
    fun getManufacturingMills() {
        showLoading(true)
        getAllManufacturingMillRemoteUseCase("") {
            it.either({
                showLoading(false)
            }, {
                showLoading(false)
                listMills.value = it
            })

        }
    }

    fun getMillById(id: String) {
        getManufacturingMillByIdUseCase(id) {
            showLoading(true)
            it.either({
                showLoading(false)
            }, {
                showLoading(false)
                millDetail.value = it
            })
        }
    }

    fun getManufacturingWorks(){
        getAllManufacturingWorkRemoteUsecase(""){
            it.either({},{
                listWorks.value = it
            })

        }
    }

    fun getUserInDepartment(){

    }

}