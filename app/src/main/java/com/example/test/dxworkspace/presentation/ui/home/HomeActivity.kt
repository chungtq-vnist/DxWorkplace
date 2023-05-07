package com.example.test.dxworkspace.presentation.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.ActivityHomeBinding
import com.example.test.dxworkspace.presentation.ui.BaseActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
//        TODO("Not yet implemented")
    }
}