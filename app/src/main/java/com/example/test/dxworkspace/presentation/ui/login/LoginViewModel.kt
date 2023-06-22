package com.example.test.dxworkspace.presentation.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test.dxworkspace.core.extensions.safeLaunch
import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.login.LoginResponseRaw
import com.example.test.dxworkspace.domain.repository.AuthRepository
import com.example.test.dxworkspace.domain.repository.ComponentRepository
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.auth.LoginByPasswordUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject
constructor(
    val loginByPasswordUseCase: LoginByPasswordUseCase,
    val repo: ComponentRepository,
    val authRepository: AuthRepository,
    val configRepository: ConfigRepository
) : BaseViewModel() {

    val isLoginSuccess = MutableLiveData<Boolean>()
    var loginResponse: LoginResponseRaw = LoginResponseRaw()

    fun login(param: LoginRequestRaw) {
        isLoading.value = true
        loginByPasswordUseCase(param) {
            it.either({
                isLoading.value = false
                isLoginSuccess.value = false
                Log.d("ViewModel", it.toString())
            }, {
                isLoading.value = false
                loginResponse = it
                isLoginSuccess.value = true
            })
        }
    }

    fun test() {
        GlobalScope.safeLaunch(Dispatchers.IO){
            val s = async {
                authRepository.getLinksCanAccessRemote(
                    configRepository.getCurrentRole().id,
                    configRepository.getUser().id
                )
            }
            s.await()
            val t = authRepository.getLinksCanAccess(configRepository.getDBName())
            println(t.size)
        }
    }

}