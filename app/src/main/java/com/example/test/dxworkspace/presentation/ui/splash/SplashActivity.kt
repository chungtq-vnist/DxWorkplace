package com.example.test.dxworkspace.presentation.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.presentation.ui.home.HomeActivity
import com.example.test.dxworkspace.presentation.ui.login.LoginActivity
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.common.SharedPreferencesHelper
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        SharedPreferencesHelper.put(Constants.APLogin.IS_LOGIN,false)
        checkLogin()
    }

    fun checkLogin() {
        Handler().postDelayed(
            {
                if (SharedPreferencesHelper.get(Constants.APLogin.IS_LOGIN,Boolean::class.java,false)) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                finishAffinity()
            }, 500
        )

    }
}