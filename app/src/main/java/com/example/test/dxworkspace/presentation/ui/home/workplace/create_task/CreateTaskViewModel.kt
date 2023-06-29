package com.example.test.dxworkspace.presentation.ui.home.workplace.create_task

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.project.ProjectResponse
import com.example.test.dxworkspace.data.entity.task.RequestCreateTask
import com.example.test.dxworkspace.data.entity.task.TaskTemplateResponse
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllOrganizationUnitRemoteUseCase
import com.example.test.dxworkspace.domain.usecase.task.GetAllProjectsUseCase
import com.example.test.dxworkspace.domain.usecase.task.GetAllTaskTemplatesUseCase
import com.example.test.dxworkspace.domain.usecase.task.RequestCreateTaskUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class CreateTaskViewModel @Inject constructor(
    private val getAllOrganizationUnitRemoteUseCase: GetAllOrganizationUnitRemoteUseCase,
    private val getAllTaskTemplatesUseCase: GetAllTaskTemplatesUseCase,
    private val getAllProjectsUseCase: GetAllProjectsUseCase,
    private val configRepository: ConfigRepository,
    private val createTaskUseCase: RequestCreateTaskUseCase
) : BaseViewModel(){


    val listOrganization = MutableLiveData<List<OrganizationUnit>>()
    val listTemplates = MutableLiveData<List<TaskTemplateResponse>>()
    val listProjects = MutableLiveData<List<ProjectResponse>>()
    val createStatus = MutableLiveData<Boolean>()

    fun getAllOrganizationUnit() {
        showLoading(true)
        getAllOrganizationUnitRemoteUseCase("") {
            it.either({
                showLoading(false)
            }, {
                showLoading(false)
                listOrganization.value = it
            })
        }
    }

    fun getAllTemplates() {
        getAllTaskTemplatesUseCase(configRepository.getUser().id) {
            it.either({
                      handleFailure(it)
            }, {
                listTemplates.value = it
            })
        }
    }
    fun getAllProjects() {
        getAllProjectsUseCase("") {
            it.either({
                      handleFailure(it)
            }, {
                listProjects.value = it
            })
        }
    }

    fun createTask(t : RequestCreateTask){
        t.creator = configRepository.getUser().id
        showLoading(true)
        createTaskUseCase(t){
            it.either({
                println("failure")
                createStatus.value = false
                showLoading(false)
            },{
                showLoading(false)
                createStatus.value = true
                println("success")
            })
        }
    }



}