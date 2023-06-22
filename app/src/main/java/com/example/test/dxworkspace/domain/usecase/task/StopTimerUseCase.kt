package com.example.test.dxworkspace.domain.usecase.task

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.data.entity.task.TaskResponseRaw
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.domain.model.task.RequestTaskModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class StopTimerUseCase  @Inject constructor(
    val taskRepository: TaskRepository,
    val configRepository: ConfigRepository
)
    : UseCase<TaskModelDetail, Pair<String,StopTimeModel>>() {
    override suspend fun run(params: Pair<String,StopTimeModel>): Either<Failure, TaskModelDetail> {
        val res = taskRepository.stopTimer(params.first, params.second)
        if(res.isRight) {
            if(res.getValue().success) return Either.Right(res.getValue().content) else return Either.Left(res.getFailure())
        } else {
            return Either.Left(res.getFailure())
        }
    }
}