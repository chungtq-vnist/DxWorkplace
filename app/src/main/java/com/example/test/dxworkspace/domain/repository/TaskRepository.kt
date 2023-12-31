package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.component.ComponentResponse
import com.example.test.dxworkspace.data.entity.project.ProjectResponse
import com.example.test.dxworkspace.data.entity.task.*
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.data.entity.timesheet.TimeSheetLogResponse

interface TaskRepository : Repository {

    suspend fun getTask(type : String , user : String ,perPage : Int , startDate : String ,
    endDate : String , aPeriodOfTime : Boolean ) : Either<Failure , TaskResponseRaw>

    suspend fun getTaskById(id : String ) : Either<Failure , TaskDetailResponseRaw>

    suspend fun startTimer(id : String , userId : String): Either<Failure, StartTimeSheetLogResponseRaw>


    suspend fun stopTimer(id : String , param : StopTimeModel) : Either<Failure , TaskDetailResponseRaw>

    suspend fun postComment(id : String , creator : String ,description : String ,index : String): Either<Failure, List<TaskAction>>

    suspend fun requestToCloseTask(id: String , body : RequestCloseTask) : Either<Failure,TaskDetailResponseRaw>

    suspend fun createTask(body : RequestCreateTask) : Either<Failure,TaskDetailResponseRaw>

    suspend fun getAllProject() : Either<Failure , List<ProjectResponse>>

    suspend fun getAllTemplates(id : String) : Either<Failure , List<TaskTemplateResponse>>

    suspend fun getTaskTimesheetLog(id:String) :Either<Failure,TimeSheetLogResponse>
}