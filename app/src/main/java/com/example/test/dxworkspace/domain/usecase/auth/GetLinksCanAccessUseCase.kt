package com.example.test.dxworkspace.domain.usecase.auth

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetLinksCanAccessUseCase @Inject constructor(
    val authRepository: AuthRepository,
    val configRepository: ConfigRepository
) : UseCase<List<LinkEntity>, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, List<LinkEntity>> {
        return coroutineScope {
            val res = async {  authRepository.getLinksCanAccess(configRepository.getDBName()) }
            Either.Right(res.await())
        }

    }

}