package com.example.test.dxworkspace.domain.usecase.task

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.task.RequestCloseTask
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.data.entity.task.TaskResponseRaw
import com.example.test.dxworkspace.data.entity.task.TaskTemplateResponse
import com.example.test.dxworkspace.domain.model.task.RequestTaskModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GetAllTaskTemplatesUseCase  @Inject constructor(
    val taskRepository: TaskRepository,
    val configRepository: ConfigRepository
)
    : UseCase<List<TaskTemplateResponse>, String >() {
    override suspend fun run(params:String): Either<Failure, List<TaskTemplateResponse>> {
        return taskRepository.getAllTemplates(params)
    }
}