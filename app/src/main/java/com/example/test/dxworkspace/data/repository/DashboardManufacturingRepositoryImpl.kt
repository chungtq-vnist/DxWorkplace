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
    override fun getNumberOfPlanByStatus(
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

    override fun getNumberOfPlanByProgress(
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

    override fun getNumberOfCommandByStatus(
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

    override fun getNumberOfCommandByProgress(
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

    override fun getNumberOfRequestByStatus(
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

    override fun getNumberOfRequestByType(
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
}