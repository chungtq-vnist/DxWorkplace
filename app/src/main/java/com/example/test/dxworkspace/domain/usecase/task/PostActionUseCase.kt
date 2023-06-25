package com.example.test.dxworkspace.domain.usecase.task

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.task.TaskAction
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.data.entity.task.TaskResponseRaw
import com.example.test.dxworkspace.data.entity.task.TimeSheetResponseRaw
import com.example.test.dxworkspace.data.entity.timesheet.StopTimeModel
import com.example.test.dxworkspace.domain.model.task.RequestTaskModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class PostActionUseCase  @Inject constructor(
    val taskRepository: TaskRepository,
    val configRepository: ConfigRepository
)
    : UseCase<List<TaskAction>, List<String>>() {
    override suspend fun run(params: List<String>): Either<Failure, List<TaskAction>> {
        return taskRepository.postComment(params[0], params[1],params[2],params[3])

    }
}