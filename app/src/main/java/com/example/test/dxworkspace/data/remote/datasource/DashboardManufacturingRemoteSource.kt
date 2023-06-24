package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.remote.api.DxApi
import javax.inject.Inject

class DashboardManufacturingRemoteSource @Inject constructor(val api: DxApi) {
    fun getNumberOfPlanByStatus(role: String, work: List<String>?, from: String?, to: String?) =
        api.getNumberOfPlanByStatus(role, work, from, to)

    fun getNumberOfPlanByProgress(role: String, work: List<String>?, from: String?, to: String?) =
        api.getNumberOfPlanByProgress(role, work, from, to)


    fun getNumberOfCommandByStatus(role: String, work: List<String>?, from: String?, to: String?) =
        api.getNumberOfCommandByStatus(role, work, from, to)

    fun getNumberOfCommandByProgress(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?
    ) =
        api.getNumberOfCommandByProgress(role, work, from, to)


    fun getNumberOfRequestByStatus(role: String, work: List<String>?, from: String?, to: String?) =
        api.getNumberOfRequestByStatus(role, work, from, to)

    fun getNumberOfRequestByType(role: String, work: List<String>?, from: String?, to: String?) =
        api.getNumberOfRequestByType(role, work, from, to)

    fun getReportGoodsQuality(
        role: String,
        work: List<String>?,
        from: String?,
        to: String?,
        fromCompare: String?,
        toCompare: String?
    ) = api.getReportGoodsQuality(role,work,from,to,fromCompare,toCompare)

}