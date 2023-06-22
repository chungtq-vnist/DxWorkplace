package com.example.test.dxworkspace.domain.usecase.task

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.task.TaskModel
import com.example.test.dxworkspace.data.entity.task.TaskResponseContent
import com.example.test.dxworkspace.data.entity.task.TaskResponseRaw
import com.example.test.dxworkspace.data.entity.task.TaskType
import com.example.test.dxworkspace.domain.model.task.RequestTaskModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.example.test.dxworkspace.domain.repository.VersionRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetTaskByTypeUseCase  @Inject constructor(
    val taskRepository: TaskRepository,
    val configRepository: ConfigRepository
)
    : UseCase<TaskResponseRaw, RequestTaskModel>() {
    override suspend fun run(params: RequestTaskModel): Either<Failure, TaskResponseRaw> =

        coroutineScope {
            val listTask = mutableListOf<TaskModel>()
            var total = 0
            val res1 = async {  taskRepository.getTask(TaskType.RESPONSIBLE,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val res2 =  async {  taskRepository.getTask(TaskType.ACCOUNTABLE,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val res3 =  async {  taskRepository.getTask(TaskType.CONSULTED,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val res4 =  async {  taskRepository.getTask(TaskType.INFORMED,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val e1 = res1.await()
            val e2 = res2.await()
            val e3 = res3.await()
            val e4 = res4.await()
            if(e1.isRight){
                listTask.addAll(e1.getValue().content.tasks ?: emptyList())
                total+=e1.getValue().content.totalCount
            }
            if(e2.isRight){
                listTask.addAll(e2.getValue().content.tasks ?: emptyList())
                total+=e2.getValue().content.totalCount
            }
            if(e3.isRight){
                listTask.addAll(e3.getValue().content.tasks ?: emptyList())
                total+=e3.getValue().content.totalCount
            }
            if(e4.isRight){
                listTask.addAll(e4.getValue().content.tasks ?: emptyList())
                total+=e4.getValue().content.totalCount
            }
            Either.Right(TaskResponseRaw(true, content = TaskResponseContent(listTask,1,total)))
        }




}