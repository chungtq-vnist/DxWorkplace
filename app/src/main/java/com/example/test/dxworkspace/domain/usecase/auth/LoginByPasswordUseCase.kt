package com.example.test.dxworkspace.domain.usecase.auth

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class LoginByPasswordUseCase @Inject constructor(val authRepository: AuthRepository) : UseCase<LoginResponseRaw , LoginRequestRaw>() {
    override suspend fun run(params: LoginRequestRaw): Either<Failure, LoginResponseRaw> {
        return authRepository.login(params)
    }

}