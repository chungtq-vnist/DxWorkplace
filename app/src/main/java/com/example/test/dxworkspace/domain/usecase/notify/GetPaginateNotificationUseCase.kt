package com.example.test.dxworkspace.domain.usecase.notify

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.notify.PaginateNotificationsResponse
import com.example.test.dxworkspace.data.entity.notify.ParamGetPageNotify
import com.example.test.dxworkspace.data.entity.report.FinancialReportContentResponse
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.NotifyRepository
import com.example.test.dxworkspace.domain.repository.ReportRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GetPaginateNotificationUseCase @Inject constructor(
    val notifyRepository: NotifyRepository,
    val configRepository: ConfigRepository
) : UseCase<PaginateNotificationsResponse, ParamGetPageNotify>() {
    override suspend fun run(params: ParamGetPageNotify): Either<Failure, PaginateNotificationsResponse> {
        return notifyRepository.getPaginateNotifications(params)
    }

}