package com.example.test.dxworkspace.domain.usecase.auth

import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.repository.ComponentRepository
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetComponentsCanAccessUseCase @Inject constructor(
    val authRepository: AuthRepository,
    val componentRepository: ComponentRepository,
    val configRepository: ConfigRepository
) : UseCase<List<ComponentEntity>, UseCase.None>() {
    override suspend fun run(params: None): Either<Failure, List<ComponentEntity>> {
        return coroutineScope {
            val res = async {  componentRepository.getAllComponentLocal(configRepository.getDBName()) }
            Either.Right(res.await())
        }

    }

}