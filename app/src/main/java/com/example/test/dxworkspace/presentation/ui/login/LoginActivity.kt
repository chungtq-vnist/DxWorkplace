package com.example.test.dxworkspace.presentation.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.ActivityLoginBinding
import com.example.test.dxworkspace.presentation.ui.BaseActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun updateUI(savedInstanceState: Bundle?) {
//        TODO("Not yet implemented")
    }
}