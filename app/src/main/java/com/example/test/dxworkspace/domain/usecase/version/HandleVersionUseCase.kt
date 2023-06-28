package com.example.test.dxworkspace.domain.usecase.version

import android.content.SharedPreferences
import com.example.test.dxworkspace.core.exception.Failure
import com.example.test.dxworkspace.core.extensions.Either
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.data.entity.version.VersionDiff
import com.example.test.dxworkspace.data.entity.version.VersionEntity
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.repository.ManufacturingWorkRepository
import com.example.test.dxworkspace.domain.repository.VersionRepository
import com.example.test.dxworkspace.domain.usecase.UseCase
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventSyncMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HandleVersionUseCase @Inject constructor(
    val versionRepository: VersionRepository,
    val configRepository: ConfigRepository,
    val sharedPreferences: SharedPreferences,
    val authRepository: AuthRepository,
    val manufacturingWorkRepository: ManufacturingWorkRepository,

    ) : UseCase<Boolean, String?>() {
    override suspend fun run(params: String?): Either<Failure, Boolean> {
        try {
            val version = versionRepository.getAllVersions()
            val versionDb = versionRepository.getAllVersionLocal(configRepository.getDBName())
            val versionDiff = compareVersion(version, versionDb)
            val roleId = configRepository.getCurrentRole().id
            val userId = configRepository.getUser().id

            versionDiff.forEach { v ->
                when (v.versionKey) {
                    Constants.VERSION_KEY.USER_ROLE, Constants.VERSION_KEY.USER -> {
                        withContext(Dispatchers.IO) {
                            // get user profile
                            authRepository.getUserProfile(userId)
                            versionRepository.saves(version.filter {
                                listOf(
                                    Constants.VERSION_KEY.USER_ROLE,
                                    Constants.VERSION_KEY.USER
                                ).contains(it.versionKey)
                            }, configRepository.getDBName())
                            EventBus.getDefault().post(EventSyncMessage(EventSyncMessage.SYNC_USER))
                        }

                    }
                    Constants.VERSION_KEY.ROLE -> {
                        withContext(Dispatchers.IO){
                            if(v.versionDB == 0 ){
                                authRepository.getAllRolesRemote()
                                versionRepository.save(version.first { it.versionKey == Constants.VERSION_KEY.ROLE },
                                    configRepository.getDBName())
                            } else {
                                authRepository.handleCompareVersionRole(v)
                            }
                        }
                    }
                    Constants.VERSION_KEY.COMPONENT, Constants.VERSION_KEY.LINK, Constants.VERSION_KEY.PRIVILEGE -> {
                        withContext(Dispatchers.IO) {
                            authRepository.getComponentCanAccessRemote(roleId, userId)
                            authRepository.getLinksCanAccessRemote(roleId, userId)
                            versionRepository.saves(version.filter {
                                listOf(
                                    Constants.VERSION_KEY.COMPONENT,
                                    Constants.VERSION_KEY.LINK,
                                    Constants.VERSION_KEY.PRIVILEGE
                                ).contains(it.versionKey)
                            }, configRepository.getDBName())
                            EventBus.getDefault().post(EventSyncMessage(EventSyncMessage.SYNC_PRIVILEGE))
                        }

                    }
                    Constants.VERSION_KEY.MANUFACTURINGWORK -> {
                       withContext(Dispatchers.IO){
                           if(v.versionDB == 0 ){
                               manufacturingWorkRepository.getAllManufacturingWorks()
                               versionRepository.save(version.first { it.versionKey == Constants.VERSION_KEY.MANUFACTURINGWORK },
                               configRepository.getDBName())
                           } else {
                               manufacturingWorkRepository.handleCompareVersion(v)
                           }
                           EventBus.getDefault().post(EventSyncMessage(EventSyncMessage.SYNC_MANUFACTURING_WORK))
                       }
                    }

                }
            }
            return Either.Right(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return Either.Right(false)
        }
    }

    private fun compareVersion(
        versionsRemote: List<VersionEntity>,
        versionsLocal: List<VersionEntity>
    ): List<VersionDiff> {
        val versionDiff: MutableList<VersionDiff> = mutableListOf()
        versionsRemote.forEach { version ->
            val check = versionsLocal.firstOrNull { v ->
                v.versionKey == version.versionKey
            }
            if (check == null) {
                versionDiff.add(VersionDiff(0, version.value, version.versionKey))
            } else if (check.value < version.value) {
                versionDiff.add(VersionDiff(check.value, version.value, version.versionKey))
            }
        }
        return versionDiff
    }

}