package com.example.test.dxworkspace.domain.usecase.role

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillEntity
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkEntity
import com.example.test.dxworkspace.data.entity.role.RoleModel
import com.example.test.dxworkspace.data.entity.task.TaskModelDetail
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingManagerRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.repository.TaskRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GetAllRoleRemoteUseCase @Inject constructor(
    val manufacturingManagerRepository: ManufacturingManagerRepository,
    val configRepository: ConfigRepository
): UseCase<List<RoleModel>, String>() {
    override suspend fun run(params: String): Either<Failure, List<RoleModel>> {
        return manufacturingManagerRepository.getAllRolesRemote()
    }
}