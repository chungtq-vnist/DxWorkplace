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
    : UseCase<MutableList<Pair<MutableList<TaskModel>,Int>>, RequestTaskModel>() {
    override suspend fun run(params: RequestTaskModel): Either<Failure, MutableList<Pair<MutableList<TaskModel>,Int>>> =

        coroutineScope {
            val listData = mutableListOf<Pair<MutableList<TaskModel>,Int>>()
            val listTask = mutableListOf<TaskModel>()
            var total = 0
            val res1 = async {  taskRepository.getTask(TaskType.RESPONSIBLE,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val res2 =  async {  taskRepository.getTask(TaskType.ACCOUNTABLE,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val res3 =  async {  taskRepository.getTask(TaskType.CONSULTED,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val res4 =  async {  taskRepository.getTask(TaskType.INFORMED,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val res5 =  async {  taskRepository.getTask(TaskType.CREATOR,params.user,params.perPage,params.startDate, params.endDate , params.aPeriodOfTime) }
            val e1 = res1.await()
            val e2 = res2.await()
            val e3 = res3.await()
            val e4 = res4.await()
            val e5 = res5.await()
            if(e1.isRight){
                val l = e1.getValue().content.tasks ?: emptyList()
                listData.add(Pair(l.toMutableList(),0))
            }
            if(e2.isRight){
                val l = e2.getValue().content.tasks ?: emptyList()
                listData.add(Pair(l.toMutableList(),1))
            }
            if(e3.isRight){
                val l = e3.getValue().content.tasks ?: emptyList()
                listData.add(Pair(l.toMutableList(),2))
            }
            if(e4.isRight){
                val l = e4.getValue().content.tasks ?: emptyList()
                listData.add(Pair(l.toMutableList(),3))
            }
            if(e5.isRight){
                val l = e5.getValue().content.tasks ?: emptyList()
                listData.add(Pair(l.toMutableList(),4))
            }
//            val listAll = mutableListOf<TaskModel>()
//            for (i in 0 until listTask.size){
//                val t = listTask[i]
//                if (listAll.find { it._id == t._id } == null) listAll.add(t)
//            }
            Either.Right(listData)
        }




}