package com.example.test.dxworkspace.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.report.FinancialReportContentResponse
import com.example.test.dxworkspace.data.entity.report.SaleReportContentResponse
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.DashboardManufacturingRemoteSource
import com.example.test.dxworkspace.data.remote.datasource.ReportRemoteSource
import com.example.test.dxworkspace.domain.repository.ReportRepository
import com.google.gson.Gson
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    val sharedPreferences: SharedPreferences,
    val context: Context,
    val gson: Gson,
    val reportRemoteSource: ReportRemoteSource
) : ReportRepository {
    override suspend fun getFinancialReport(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ): Either<Failure, FinancialReportContentResponse> {
        return requestApi(
            reportRemoteSource.getFinancialReport(from, to, fromCompare, toCompare),
            {
                if (it.success) {
                    it.content ?: FinancialReportContentResponse()
                } else FinancialReportContentResponse()
            },
            FinancialReportContentResponse()
        )
    }

    override suspend fun getSaleReport(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ): Either<Failure, SaleReportContentResponse> {
        return requestApi(
            reportRemoteSource.getSaleReport(from, to, fromCompare, toCompare),
            {
                if (it.success) {
                    it.content ?: SaleReportContentResponse()
                } else SaleReportContentResponse()
            },
            SaleReportContentResponse()
        )
    }
}