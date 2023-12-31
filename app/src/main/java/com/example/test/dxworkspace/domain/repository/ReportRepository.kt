package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.report.*

interface ReportRepository {
    suspend fun getFinancialReport(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ): Either<Failure, FinancialReportContentResponse>

    suspend fun getSaleReport(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ): Either<Failure, SaleReportContentResponse>

    suspend fun getNumberPlanCompletedOnSchedule(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ) : Either<Failure, PlanCompletedOnScheduleContent>

    suspend fun getWarehouseReport(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ) : Either<Failure, List<WarehouseReportModel>>
}