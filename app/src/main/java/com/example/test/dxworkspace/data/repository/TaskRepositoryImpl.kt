package com.example.test.dxworkspace.data.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.project.ProjectResponse
import com.example.test.dxworkspace.data.entity.task.*
import com.example.test.dxworkspace.data.entity.timesheet.StartTimeModel
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.data.entity.timesheet.TimeSheetLogResponse
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.TaskRemoteSource
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    val taskRemoteSource: TaskRemoteSource
) : TaskRepository {
    override suspend fun getTask(
        type: String,
        user: String,
        perPage: Int,
        startDate: String,
        endDate: String,
        aPeriodOfTime: Boolean
    ): Either<Failure, TaskResponseRaw> {
        val result = requestApi(
            taskRemoteSource.getTask(type, user, perPage, startDate, endDate, aPeriodOfTime),
            TaskResponseRaw()
        )
        return result
    }

    override suspend fun getTaskById(id: String): Either<Failure, TaskDetailResponseRaw> {
        return requestApi(taskRemoteSource.getTaskById(id),TaskDetailResponseRaw())
    }

    override suspend fun startTimer(id: String, userId: String): Either<Failure, StartTimeSheetLogResponseRaw> {
        return requestApi(taskRemoteSource.startTimer(id , StartTimeModel(userId,userId)),
            StartTimeSheetLogResponseRaw()
        )
    }

    override suspend fun stopTimer(
        id: String,
        param: StopTimeModel
    ): Either<Failure, TaskDetailResponseRaw> {
        return requestApi(taskRemoteSource.stopTimer(id,param), TaskDetailResponseRaw())
    }

    override suspend fun postComment(
        id: String,
        creator: String,
        description: String,
        index: String
    ): Either<Failure, List<TaskAction>> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("creator", creator)
            .addFormDataPart("description", description)
            .addFormDataPart("index", index)
            .build()
        return requestApi(
            taskRemoteSource.postAction(id, requestBody), {
                if(it.success) it.content ?: listOf<TaskAction>() else listOf<TaskAction>()
            },
            listOf<TaskAction>()
        )
    }

    override suspend fun requestToCloseTask(
        id: String,
        body: RequestCloseTask
    ): Either<Failure, TaskDetailResponseRaw> {
        return requestApi(
            taskRemoteSource.requestToCloseTask(id,body),
            TaskDetailResponseRaw()
        )
    }

    override suspend fun getTaskTimesheetLog(id: String): Either<Failure, TimeSheetLogResponse> {
        return requestApi(
            taskRemoteSource.getTaskTimesheetLog(id),
            {
            if(it.success) it.content ?: TimeSheetLogResponse() else TimeSheetLogResponse()
            },
            TimeSheetLogResponse()
        )
    }

    override suspend fun createTask(body: RequestCreateTask): Either<Failure, TaskDetailResponseRaw> {
//        val requestBody = RequestBody.create(MediaType.parse("application/json"))
//            MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("data", "hi")
//            .ad
//            .build()
        val data = Gson().toJson(body)
        val param = HashMap<String,String>()
        param.put("data",data)
        return requestApi(
            taskRemoteSource.createTask(param),
            TaskDetailResponseRaw()
        )
    }

    override suspend fun getAllProject(): Either<Failure, List<ProjectResponse>> {
        return requestApi(
            taskRemoteSource.getAllProject(),
            {
                if(it.success) it.content ?: listOf() else listOf()
            } , listOf()
        )
    }

    override suspend fun getAllTemplates(id: String): Either<Failure, List<TaskTemplateResponse>> {
        return requestApi(
            taskRemoteSource.getAllTemplate(id),
            {
                if(it.success) it.content?.docs ?: listOf() else listOf()
            } , listOf()
        )
    }

}