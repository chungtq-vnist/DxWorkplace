package com.example.test.dxworkspace.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.LogHelper
import com.example.test.dxworkspace.core.extensions.encryptMessage
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.component.ComponentEntity
import com.example.test.dxworkspace.data.entity.link.LinkEntity
import com.example.test.dxworkspace.data.entity.login.LoginRequestRaw
import com.example.test.dxworkspace.data.entity.role.RoleEntity
import com.example.test.dxworkspace.data.entity.user.UserEntity
import com.example.test.dxworkspace.databinding.ActivityLoginBinding
import com.example.test.dxworkspace.presentation.ui.BaseActivity
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.isValidEmail
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import javax.inject.Inject
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.presentation.ui.home.HomeActivity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    @Inject
    lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG","oncreate login activity")
        viewModel = viewModel(viewModelFactory) {
            observe(isLoginSuccess) {
                Log.d("TAG","observer login status")
                if (it == false) showToast(EventToast(isFail = true, text = "Login error"))
                if (it == true) handleSuccess()
            }
        }
        observeLoading(viewModel)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.apply {
            btnLogin.setOnClickListener {
                val param = LoginRequestRaw(
                    edtUsername.getTextz(),
                    edtPassword.getTextz(),
                    edtPortal.getTextz()
                )
                viewModel.login(param)
            }
            edtUsername.doAfterTextChanged {
                checkEnableLogin()
            }
            edtPassword.doAfterTextChanged {
                checkEnableLogin()
            }
            edtPortal.doAfterTextChanged {
                checkEnableLogin()
            }
            btnResetPassword.setOnClickListener {
//                LogHelper.debug(encryptMessage("6420000ff2102b282bdcf5f6"))
//                testRealmDB()
                viewModel.test()
            }
        }
    }

    fun checkEnableLogin() {
        binding.btnLogin.isEnabled =
            binding.edtPassword.getTextz().length >= 4 && isValidEmail(binding.edtUsername.getTextz()) && binding.edtPortal.getTextz()
                .isNotEmpty()
    }

    fun handleSuccess() {
        Log.d("TAG", "token : " + viewModel.loginResponse.content.token)
        val loginResult = viewModel.loginResponse.content
        sharedPreferences[Constants.APLogin.TOKEN] = loginResult.token
        sharedPreferences[Constants.APLogin.IS_LOGGED] = true
        sharedPreferences[Constants.APLogin.CURRENT_USER] = gson.toJson(loginResult.user)
        sharedPreferences[Constants.APLogin.CURRENT_ROLE] = gson.toJson(loginResult.user.roles[0].roleId)
        sharedPreferences[Constants.APLogin.CURRENT_ROLE_ID] = loginResult.user.roles[0].roleId.id
        sharedPreferences[Constants.APLogin.CURRENT_PAGE] = "/home"
        sharedPreferences[Constants.APLogin.LOGIN_RESPONSE_INFO] = gson.toJson(loginResult)
        val intent = Intent(this, HomeActivity::class.java)

        startActivity(intent)
        finishAffinity()
    }

    fun testRealmDB(){
        val config = RealmConfiguration.create(
            schema = setOf(UserEntity::class, ComponentEntity::class, LinkEntity::class,RoleEntity::class)
        )


        val realm : Realm = Realm.open(config)
        realm.writeBlocking {
            val component = ComponentEntity().apply {
                _id = "123"
                name = "HI bro"
            }
            this.copyToRealm(component,UpdatePolicy.ALL)
        }
        val t = realm.query<ComponentEntity>().find().toList()
        println(t.first().name.toString())

    }
}