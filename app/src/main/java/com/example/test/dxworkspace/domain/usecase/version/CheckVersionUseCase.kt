package com.example.test.dxworkspace.domain.usecase.version

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.VersionRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import javax.inject.Inject

class CheckVersionUseCase  @Inject constructor(
    val versionRepository: VersionRepository,
    val configRepository: ConfigRepository
    )
    : UseCase<UseCase.None, String?>() {
    override suspend fun run(params: String?): Either<Failure, None> {
         versionRepository.fetchAllDataLatest(configRepository.getDBName())
         return Either.Right(None())
    }

}