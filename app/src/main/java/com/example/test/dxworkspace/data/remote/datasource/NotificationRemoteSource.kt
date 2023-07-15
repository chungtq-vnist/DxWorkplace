package com.example.test.dxworkspace.data.remote.datasource

import com.example.test.dxworkspace.data.entity.notify.ParamGetPageNotify
import com.example.test.dxworkspace.data.entity.notify.ParamMarkNotifyReaded
import com.example.test.dxworkspace.data.remote.api.DxApi
import javax.inject.Inject

class NotificationRemoteSource @Inject constructor(val api : DxApi) {

    fun getPaginateNotifications(data : ParamGetPageNotify) = api.getPageNotifications(data)

    fun markNotifyReaded(data : ParamMarkNotifyReaded) = api.markNotifyReaded(data)

}