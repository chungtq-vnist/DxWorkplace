package com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.FragmentWorkScheduleBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule.adapter.WorkSchedulePagerAdapter
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class WorkScheduleFragment : BaseFragment<FragmentWorkScheduleBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: WorkScheduleViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    lateinit var pagerAdapter : WorkSchedulePagerAdapter

    override fun getLayoutId(): Int = R.layout.fragment_work_schedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            tabLayout.addTab(tabLayout.newTab().setText("Xưởng"),0)
            tabLayout.addTab(tabLayout.newTab().setText("Nhân viên"),1)
            pagerAdapter = WorkSchedulePagerAdapter(childFragmentManager,lifecycle)
            viewpager.adapter = pagerAdapter
            viewpager.isUserInputEnabled = false
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewpager.setCurrentItem(tab?.position ?: 0)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
                }

            })
        }
    }

}