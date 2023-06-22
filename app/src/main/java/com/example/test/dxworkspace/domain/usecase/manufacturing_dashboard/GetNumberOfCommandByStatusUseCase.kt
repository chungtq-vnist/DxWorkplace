package com.example.test.dxworkspace.domain.usecase.manufacturing_dashboard

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.*
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.DashboardManufacturingRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GetNumberOfCommandByStatusUseCase @Inject constructor(
    val dashboardManufacturingRepository: DashboardManufacturingRepository,
    val configRepository: ConfigRepository
): UseCase<DashboardManufacturingCommandByStatus, ModelRequestDashboardPlan>() {
    override suspend fun run(params: ModelRequestDashboardPlan): Either<Failure, DashboardManufacturingCommandByStatus> {
        return dashboardManufacturingRepository.getNumberOfCommandByStatus(params.role,params.works,params.fromDate,params.toDate)
    }

}