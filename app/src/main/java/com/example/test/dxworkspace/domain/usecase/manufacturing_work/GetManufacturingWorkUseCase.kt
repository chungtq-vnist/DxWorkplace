package com.example.test.dxworkspace.domain.usecase.manufacturing_work

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GetManufacturingWorkUseCase @Inject constructor(
    val manufacturingWorkRepository: ManufacturingWorkRepository,
    val configRepository: ConfigRepository
): UseCase<List<ManufacturingWorkEntity>, String>() {
    override suspend fun run(params: String): Either<Failure, List<ManufacturingWorkEntity>> {
        val t = manufacturingWorkRepository.getAllFromDb()
        return Either.Right(t)
    }

}