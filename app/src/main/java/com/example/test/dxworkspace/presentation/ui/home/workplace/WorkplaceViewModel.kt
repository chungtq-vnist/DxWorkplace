package com.example.test.dxworkspace.presentation.ui.home.workplace

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.task.RequestCloseTask
import com.example.test.dxworkspace.data.entity.task.TaskModel
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.domain.model.task.RequestTaskModel
import com.example.test.dxworkspace.domain.usecase.UseCase
import com.example.test.dxworkspace.domain.usecase.auth.GetLinksCanAccessUseCase
import com.example.test.dxworkspace.domain.usecase.auth.LogoutUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.domain.usecase.task.*
import javax.inject.Inject
import javax.inject.Singleton

class WorkplaceViewModel @Inject constructor(
    private val getLinksCanAccessUseCase: GetLinksCanAccessUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getTaskByTypeUseCase: GetTaskByTypeUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val startTimerUseCase: StartTimerUseCase,
    private val stopTimerUseCase: StopTimerUseCase,
    private val sharedPreferences: SharedPreferences,
    private val postActionUseCase: PostActionUseCase,
    private val requestCloseTaskUseCase: RequestCloseTaskUseCase,
    ) : BaseViewModel() {
    val linksCanAccess = MutableLiveData<List<LinkEntity>>()
    val logoutSuccess = MutableLiveData<Boolean>()
    val listTask = MutableLiveData<MutableList<TaskModel>>()
    val taskSelected = MutableLiveData<TaskModelDetail>()
    var taskIdTimer = ""
    var startDate = ""
    var endDate = ""
    val stopSuccess = MutableLiveData<Boolean>()
    fun getLinksCanAccess(){
        getLinksCanAccessUseCase(UseCase.None()){
            it.either({

            },{
                linksCanAccess.value = it
            })
        }
    }

    fun logout(){
        logoutUseCase(""){
            it.either({
                logoutSuccess.value = false
            },{
                logoutSuccess.value = true
            })
        }
    }

    fun getAllTask(param : RequestTaskModel){
        showLoading(true)
        getTaskByTypeUseCase(param){
            it.either({
                showLoading(false)
                listTask.value = mutableListOf()
            },{
                showLoading(false)
                if(it.success) listTask.value = it.content.tasks?.toMutableList()
            })
        }
    }

    fun getTaskById(id:String){
        showLoading(true)
        getTaskByIdUseCase(id){
            it.either({
                showLoading(false)
                taskSelected.value = TaskModelDetail()
            },{
                showLoading(false)
                taskSelected.value = it
            })
        }
    }

    fun startTimer(taskId : String,userId:String){
        startTimerUseCase(Pair(taskId,userId)){
            it.either({},{
                sharedPreferences[Constants.TIMERID_COUNTING] = it.content?.timesheetLogs?.first()?._id
            })
        }
    }

    fun stopTimer(taskId: String, p: StopTimeModel) {
        showLoading(true)
        stopTimerUseCase(Pair(taskId, p)) {
            it.either({
                showLoading(false)
                handleFailure(it)
            }, {
                showLoading(false)
                stopSuccess.value = true
            })
        }
    }

    fun postAction(taskId : String , creator : String ,description : String ,index : Int){
        postActionUseCase(listOf(taskId,creator,description,index.toString())){
            it.either({},
                {
                    val t = taskSelected.value
                    val listaction = t?.taskActions?.toMutableList()
                    t?.taskActions = it
                    taskSelected.value = t!!
                })
        }
    }

    fun requestCloseTask(taskId : String , body : RequestCloseTask){
        showLoading(true)
        requestCloseTaskUseCase(Pair(taskId,body)){
            it.either({
                showLoading(false)
                val t = taskSelected.value
                taskSelected.value = t ?: TaskModelDetail()
            },{
                showLoading(false)
                taskSelected.value = it
            })

        }
    }

}