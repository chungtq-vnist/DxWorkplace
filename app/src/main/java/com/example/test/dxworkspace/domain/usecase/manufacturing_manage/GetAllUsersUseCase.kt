package com.example.test.dxworkspace.domain.usecase.manufacturing_manage

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingManagerRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    val manufacturingManagerRepository: ManufacturingManagerRepository,
    val configRepository: ConfigRepository
): UseCase< List<UserProfileResponse>, String>() {
    override suspend fun run(params:String): Either<Failure,  List<UserProfileResponse>> {
       return manufacturingManagerRepository.getAllUser()
    }

}