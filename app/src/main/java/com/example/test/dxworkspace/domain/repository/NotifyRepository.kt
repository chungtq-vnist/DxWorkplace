package com.example.test.dxworkspace.domain.repository

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.login.RoleResponse
import com.example.test.dxworkspace.data.entity.login.RoleResponseRaw
import com.example.test.dxworkspace.data.entity.login.UserResponseRaw
import com.example.test.dxworkspace.data.entity.notify.PaginateNotificationsResponse
import com.example.test.dxworkspace.data.entity.notify.ParamGetPageNotify
import com.example.test.dxworkspace.data.entity.notify.ParamMarkNotifyReaded
import com.example.test.dxworkspace.data.entity.role.RoleEntity

interface NotifyRepository {
    fun getPaginateNotifications(data : ParamGetPageNotify) : Either<Failure, PaginateNotificationsResponse>

    fun markNotifyReaded(data : ParamMarkNotifyReaded) : Either<Failure,Boolean>
}