package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.*

interface DashboardManufacturingRepository {
    suspend fun getNumberOfPlanByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingPlanByStatus>
   suspend fun getNumberOfPlanByProgress(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingPlanByProgress>

   suspend fun getNumberOfCommandByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingCommandByStatus>
   suspend fun getNumberOfCommandByProgress(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingCommandByProgress>


   suspend fun getNumberOfRequestByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingRequestByStatus>
   suspend fun getNumberOfRequestByType(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingRequestByType>

   suspend fun getReportGoodsQuality(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?,
        fromCompare : String? ,
        toCompare : String?
    ) : Either<Failure,DashboardManufacturingQualityGoodsContent>
}