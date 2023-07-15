package com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.work_schedule.ParamCreateWorkSchedule
import com.example.test.dxworkspace.data.entity.work_schedule.ParamWorkSchedule
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleDetailModel
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.*
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class WorkScheduleViewModel @Inject constructor(
    private val getDetailWorkScheduleUseCase: GetDetailWorkScheduleUseCase,
    private val getAllManufacturingMillRemoteUseCase: GetAllManufacturingMillRemoteUseCase,
    private val getUserInManufacturingWorkUseCase: GetUserInManufacturingWorkUseCase,
    private val createScheduleUseCase: CreateWorkScheduleUseCase
) : BaseViewModel() {

    val listMills = MutableLiveData<List<ManufacturingMillModel>>()
    val workSchedule = MutableLiveData<WorkScheduleDetailModel>()
    val listUser = MutableLiveData<List<SubUserBasicModel>>()
    val statusCreated = MutableLiveData<Boolean>()
    fun getWorkSchedule(data: ParamWorkSchedule) {
        getDetailWorkScheduleUseCase(data) {
            it.either({
                      handleFailure(it)
            }, {
                workSchedule.value = it.firstOrNull()
            })
        }
    }

    fun getManufacturingMills() {
        showLoading(true)
        getAllManufacturingMillRemoteUseCase("") {
            it.either({
                showLoading(false)
            }, {
                showLoading(false)
                listMills.value = it.filter { it.status == 1 }
            })

        }
    }

    fun getUserInWork(role: String) {
        showLoading(true)
        getUserInManufacturingWorkUseCase(role) {
            it.either({
                showLoading(false)
            }, {
                showLoading(false)
                listUser.value = it
            })

        }
    }

    fun createSchedule(data: ParamCreateWorkSchedule){
        showLoading(true)
        createScheduleUseCase(data){
            it.either({
                showLoading(false)
                statusCreated.value = false
            },{
                showLoading(false)
                statusCreated.value= true
            })
        }
    }
}