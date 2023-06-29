package com.example.test.dxworkspace.domain.usecase.task

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.task.RequestCloseTask
import com.example.test.dxworkspace.data.entity.task.RequestCreateTask
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.data.entity.task.TaskResponseRaw
import com.example.test.dxworkspace.domain.model.task.RequestTaskModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class RequestCreateTaskUseCase  @Inject constructor(
    val taskRepository: TaskRepository,
    val configRepository: ConfigRepository
)
    : UseCase<Boolean, RequestCreateTask>() {
    override suspend fun run(params:RequestCreateTask): Either<Failure, Boolean> {
        val res = taskRepository.createTask(params)
        if(res.isRight) {
            if(res.getValue().success) return Either.Right(true) else return Either.Left(res.getFailure())
        } else {
            return Either.Left(res.getFailure())
        }
    }
}