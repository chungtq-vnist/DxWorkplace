package com.example.test.dxworkspace.domain.usecase.manufacturing_manage

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandEntity
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubSaleOrder
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillEntity
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.UserRoleInOrganizationUnit
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.data.entity.work_schedule.WorkScheduleModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingManagerRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GeWorkScheduleOfMillByDateUseCase @Inject constructor(
    val manufacturingManagerRepository: ManufacturingManagerRepository,
    val configRepository: ConfigRepository
): UseCase< List<WorkScheduleModel>, Triple<List<String>,String,String>>() {
    override suspend fun run(params:Triple<List<String>,String,String>): Either<Failure,  List<WorkScheduleModel>> {
       return manufacturingManagerRepository.getWorkScheduleOfMillByDate(params.first,params.second,params.third)
    }

}