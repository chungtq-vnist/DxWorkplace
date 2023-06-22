package com.example.test.dxworkspace.domain.usecase.auth

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class LogoutUseCase @Inject constructor(val authRepository: AuthRepository) :
    UseCase<Boolean , String>() {
    override suspend fun run(params: String): Either<Failure, Boolean> {
        return authRepository.logout()
    }

}