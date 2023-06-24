package com.example.test.dxworkspace.domain.usecase.manufacturing_dashboard

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.dashboard_manufacturing.*
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.DashboardManufacturingRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import com.example.test.dxworkspace.presentation.utils.common.roundPercent
import javax.inject.Inject

class GetReportGoodsQualityUseCase @Inject constructor(
    val dashboardManufacturingRepository: DashboardManufacturingRepository,
    val configRepository: ConfigRepository
) : UseCase<MutableList<QualityGoodsCompare>, ModelRequestDashboardGoodQuality>() {
    override suspend fun run(params: ModelRequestDashboardGoodQuality): Either<Failure, MutableList<QualityGoodsCompare>> {
        val res = dashboardManufacturingRepository.getReportGoodsQuality(
            params.role,
            params.works,
            params.fromDate,
            params.toDate,
            params.fromDateCompare,
            params.toDateCompare
        )
        if (res.isRight) {
            val data = res.getValue().data
            val compare = res.getValue().dataCompare
            val result = mutableListOf<QualityGoodsCompare>()
            data?.forEach { k ->
                val p = roundPercent(k.numberOfProducts ?: 0, (k.numberOfProducts ?: 0 ) +( k.numberOfWaste ?: 0))
                val s = QualityGoodsCompare(k.numberOfProducts,k.numberOfWaste,0,0,k.good
                ,p,null)
                result.add(s)
            }
            compare?.forEach { k ->
                val t = result.find { it.goods?._id == k.good?._id }
                if (t == null) {
                    val p = roundPercent(k.numberOfProducts ?: 0, (k.numberOfProducts ?: 0 ) +( k.numberOfWaste ?: 0))
                    val s = QualityGoodsCompare(0,0,k.numberOfProducts,k.numberOfWaste,k.good,
                       null,p )
                    result.add(s)
                } else {
                    t.numberOfProductsCompare = k.numberOfProducts
                    t.numberOfWasteCompare = k.numberOfWaste
                    t.qualityPercentConpare = roundPercent(k.numberOfProducts ?: 0, (k.numberOfProducts ?: 0 ) +( k.numberOfWaste ?: 0))
                }
            }
            return Either.Right(result)
        } else {
            return Either.Left(res.getFailure())
        }
    }

}