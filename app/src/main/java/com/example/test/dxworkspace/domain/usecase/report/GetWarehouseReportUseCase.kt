package com.example.test.dxworkspace.domain.usecase.report

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.ModelRequestDashboardGoodQuality
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.QualityGoodsCompare
import com.example.test.dxworkspace.data.entity.report.*
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.DashboardManufacturingRepository
import com.example.test.dxworkspace.domain.repository.ReportRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GetWarehouseReportUseCase @Inject constructor(
    val reportRepository: ReportRepository,
    val configRepository: ConfigRepository
) : UseCase<List<WarehouseReportModel>, ReportRequestModel>() {
    override suspend fun run(params: ReportRequestModel): Either<Failure, List<WarehouseReportModel>> {
        return reportRepository.getWarehouseReport(
            params.fromDate,
            params.toDate,
            params.fromDateCompare,
            params.toDateCompare
        )
    }
}