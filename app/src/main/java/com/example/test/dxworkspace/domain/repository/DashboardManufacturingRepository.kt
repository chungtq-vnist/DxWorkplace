package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.*

interface DashboardManufacturingRepository {
    fun getNumberOfPlanByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingPlanByStatus>
    fun getNumberOfPlanByProgress(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingPlanByProgress>

    fun getNumberOfCommandByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingCommandByStatus>
    fun getNumberOfCommandByProgress(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingCommandByProgress>


    fun getNumberOfRequestByStatus(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingRequestByStatus>
    fun getNumberOfRequestByType(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ): Either<Failure, DashboardManufacturingRequestByType>
}