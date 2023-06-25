package com.example.test.dxworkspace.domain.usecase.report

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.ModelRequestDashboardGoodQuality
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.QualityGoodsCompare
import com.example.test.dxworkspace.data.entity.report.FinancialReportContentResponse
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.DashboardManufacturingRepository
import com.example.test.dxworkspace.domain.repository.ReportRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GetFinancialReportUseCase @Inject constructor(
    val reportRepository: ReportRepository,
    val configRepository: ConfigRepository
) : UseCase<FinancialReportContentResponse, ReportRequestModel>() {
    override suspend fun run(params: ReportRequestModel): Either<Failure, FinancialReportContentResponse> {
        return reportRepository.getFinancialReport(
            params.fromDate,
            params.toDate,
            params.fromDateCompare,
            params.toDateCompare
        )
    }
}