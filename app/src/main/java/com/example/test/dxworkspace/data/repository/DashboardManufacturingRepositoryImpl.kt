package com.example.test.dxworkspace.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.*
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.DashboardManufacturingRemoteSource
import com.example.test.dxworkspace.domain.repository.DashboardManufacturingRepository
import com.google.gson.Gson
import javax.inject.Inject

class DashboardManufacturingRepositoryImpl @Inject constructor(
    val sharedPreferences: SharedPreferences,
    val context: Context,
    val gson: Gson,
    val dashboardManufacturingRemoteSource: DashboardManufacturingRemoteSource
) : DashboardManufacturingRepository {
    override suspend fun getNumberOfPlanByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingPlanByStatus> {
        return requestApi(dashboardManufacturingRemoteSource.getNumberOfPlanByStatus(
            role,
            work,
            from,
            to
        ),
            {
                if (it.success == true) {
                    it.content ?: DashboardManufacturingPlanByStatus()
                } else DashboardManufacturingPlanByStatus()
            }, DashboardManufacturingPlanByStatus()
        )
    }

    override suspend fun getNumberOfPlanByProgress(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingPlanByProgress> {
        return requestApi(dashboardManufacturingRemoteSource.getNumberOfPlanByProgress(
            role,
            work,
            from,
            to
        ),
            {
                if (it.success == true) {
                    it.content ?: DashboardManufacturingPlanByProgress()
                } else DashboardManufacturingPlanByProgress()
            }, DashboardManufacturingPlanByProgress()
        )
    }

    override suspend fun getNumberOfCommandByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingCommandByStatus> {
        return requestApi(dashboardManufacturingRemoteSource.getNumberOfCommandByStatus(
            role,
            work,
            from,
            to
        ),
            {
                if (it.success == true) {
                    it.content ?: DashboardManufacturingCommandByStatus()
                } else DashboardManufacturingCommandByStatus()
            }, DashboardManufacturingCommandByStatus()
        )
    }

    override suspend fun getNumberOfCommandByProgress(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingCommandByProgress> {
        return requestApi(dashboardManufacturingRemoteSource.getNumberOfCommandByProgress(
            role,
            work,
            from,
            to
        ),
            {
                if (it.success == true) {
                    it.content ?: DashboardManufacturingCommandByProgress()
                } else DashboardManufacturingCommandByProgress()
            }, DashboardManufacturingCommandByProgress()
        )
    }

    override suspend fun getNumberOfRequestByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingRequestByStatus> {
        return requestApi(dashboardManufacturingRemoteSource.getNumberOfRequestByStatus(
            role,
            work,
            from,
            to
        ),
            {
                if (it.success == true) {
                    it.content ?: DashboardManufacturingRequestByStatus()
                } else DashboardManufacturingRequestByStatus()
            }, DashboardManufacturingRequestByStatus()
        )    }

    override suspend fun getNumberOfRequestByType(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingRequestByType> {
        return requestApi(dashboardManufacturingRemoteSource.getNumberOfRequestByType(
            role,
            work,
            from,
            to
        ),
            {
                if (it.success == true) {
                    it.content ?: DashboardManufacturingRequestByType()
                } else DashboardManufacturingRequestByType()
            }, DashboardManufacturingRequestByType()
        )    }

    override  suspend  fun getReportGoodsQuality(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ): Either<Failure, DashboardManufacturingQualityGoodsContent> {
        return requestApi(dashboardManufacturingRemoteSource.getReportGoodsQuality(
            role, work, from, to, fromCompare, toCompare
        ) , {
            if (it.success == true) {
                it.content ?: DashboardManufacturingQualityGoodsContent()
            } else DashboardManufacturingQualityGoodsContent()
        } , DashboardManufacturingQualityGoodsContent())
    }
}