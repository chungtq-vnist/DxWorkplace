package com.example.test.dxworkspace.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.notify.PaginateNotificationsResponse
import com.example.test.dxworkspace.data.entity.notify.ParamGetPageNotify
import com.example.test.dxworkspace.data.entity.notify.ParamMarkNotifyReaded
import com.example.test.dxworkspace.data.remote.api.requestApi
import com.example.test.dxworkspace.data.remote.datasource.NotificationRemoteSource
import com.example.test.dxworkspace.data.remote.datasource.ReportRemoteSource
import com.example.test.dxworkspace.domain.repository.NotifyRepository
import com.example.test.dxworkspace.domain.repository.ReportRepository
import com.google.gson.Gson
import javax.inject.Inject

class NotifyRepositoryImpl @Inject constructor(
    val sharedPreferences: SharedPreferences,
    val context: Context,
    val gson: Gson,
    val notificationRemoteSource: NotificationRemoteSource
) : NotifyRepository {

    override fun getPaginateNotifications(data: ParamGetPageNotify): Either<Failure, PaginateNotificationsResponse> {
        return requestApi(
            notificationRemoteSource.getPaginateNotifications(data),
            {
                if(it.success) it.content ?: PaginateNotificationsResponse() else PaginateNotificationsResponse()
            },
            PaginateNotificationsResponse()
        )
    }

    override fun markNotifyReaded(data: ParamMarkNotifyReaded): Either<Failure, Boolean> {
        return requestApi(
            notificationRemoteSource.markNotifyReaded(data),
            {
                true
            } , true
        )
    }
}