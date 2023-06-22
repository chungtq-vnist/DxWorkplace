package com.example.test.dxworkspace.data.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.task.TaskDetailResponseRaw
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.data.entity.task.TaskResponseRaw
import com.example.test.dxworkspace.data.entity.task.TimeSheetResponseRaw
import com.example.test.dxworkspace.data.entity.timesheet.StartTimeModel
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.TaskRemoteSource
import com.example.test.dxworkspace.domain.repository.TaskRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    override suspend fun startTimer(id: String, userId: String): Either<Failure, TaskDetailResponseRaw> {
        return requestApi(taskRemoteSource.startTimer(id , StartTimeModel(userId,userId)),
            TaskDetailResponseRaw()
        )
    }

    override suspend fun stopTimer(
        id: String,
        param: StopTimeModel
    ): Either<Failure, TaskDetailResponseRaw> {
        return requestApi(taskRemoteSource.stopTimer(id,param), TaskDetailResponseRaw())
    }

    override suspend fun postComment(id: String, creator: String, description: String, index: String): Either<Failure, TimeSheetResponseRaw> {
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("creator", creator)
            .addFormDataPart("description", description)
            .addFormDataPart("index", index)
            .build()
        return requestApi(taskRemoteSource.postAction(id,requestBody),
            TimeSheetResponseRaw())
    }

}