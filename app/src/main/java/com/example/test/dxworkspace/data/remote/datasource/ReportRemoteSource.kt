package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.remote.api.DxApi
import javax.inject.Inject

class ReportRemoteSource @Inject constructor(val api: DxApi) {

    fun getFinancialReport(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ) = api.getFinancialReport(from, to, fromCompare, toCompare)

    fun getSaleReport(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ) = api.getSaleReport(from,to, fromCompare,toCompare)

    fun getNumberPlanCompletedOnSchedule(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ) = api.getNumberPlanCompletedOnSchedule(from,to,fromCompare,toCompare)

    fun getWarehouseReport(
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ) = api.getWarehouseReport(from,to,fromCompare,toCompare)
}