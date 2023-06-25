package com.example.test.dxworkspace.presentation.ui.home.workplace

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.data.entity.login.RoleResponse
import com.example.test.dxworkspace.data.local.database.RealmManager
import com.example.test.dxworkspace.data.local.preferences.AppPreferences.set
import com.example.test.dxworkspace.databinding.FragmentSelectRoleBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeActivity
import com.example.test.dxworkspace.presentation.ui.home.workplace.adapter.SelectRoleAdapter
import com.example.test.dxworkspace.presentation.ui.splash.SplashActivity
import com.example.test.dxworkspace.presentation.utils.common.Constants
import javax.inject.Inject

class SelectRoleFragment : BaseFragment<FragmentSelectRoleBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_select_role
    }

    @Inject
    lateinit var configRepository: ConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    val adapter by lazy{ SelectRoleAdapter()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var roleNow = configRepository.getCurrentRole()
        var listRole = configRepository.getUser().roles.map { it.roleId }
        binding?.apply {
            btnTimeSheet.isSelected = true
            btnTimeSheet.isEnabled = true

            ivBack.setOnClickListener { onBackPress() }
            rcvRole.adapter = adapter
            adapter.roleNow = roleNow.id
            adapter.items = listRole.toMutableList()
            btnTimeSheet.setOnClickListener {
                if(roleNow.id == adapter.roleNow) onBackPress()
                else {
                    sharedPreferences[Constants.APLogin.CURRENT_ROLE] = gson.toJson(listRole.first { it.id == adapter.roleNow })
                    sharedPreferences[Constants.APLogin.CURRENT_ROLE_ID] = adapter.roleNow
                    RealmManager.closeDb()
                    val intent = Intent(activity, SplashActivity::class.java)
                    startActivity(intent)
                    activity?.finishAffinity()
                }
            }
        }
    }
}