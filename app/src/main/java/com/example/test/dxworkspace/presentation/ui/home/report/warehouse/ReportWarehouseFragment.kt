package com.example.test.dxworkspace.presentation.ui.home.report.warehouse

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.report.ReportRequestModel
import com.example.test.dxworkspace.databinding.FragmentDashboardWarehouseBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.ReportViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.adapter.WarehouseReportTabAdapter
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.common.registerGreen
import com.example.test.dxworkspace.presentation.utils.common.unregisterGreen
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.google.android.material.tabs.TabLayout
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class ReportWarehouseFragment : BaseFragment<FragmentDashboardWarehouseBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard_warehouse
    }

    @Inject
    lateinit var homeViewModel : HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ReportViewModel

    lateinit var tabAdapter : WarehouseReportTabAdapter

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }



    fun onBusEvent(event : EventUpdate){
        when(event.type){
            EventUpdate.UPDATE_DASHBOARD_MANUFACTURING -> {
                binding?.tvRangeTime?.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
//                getDataWarehouseReport()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(failure) {
                showToast(EventToast(R.string.error_notification))
            }
        }
        observe(homeViewModel.statusReport){
                binding?.pullToRefresh?.isRefreshing = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabAdapter = WarehouseReportTabAdapter(childFragmentManager,lifecycle)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            viewpager.adapter = tabAdapter
            viewpager.isUserInputEnabled = false
//            tabLayout.addTab(tabLayout.newTab().setText("Vòng quay hàng tồn kho"), 0)
            tabLayout.addTab(tabLayout.newTab().setText("Sản phẩm"), 0)
            tabLayout.addTab(tabLayout.newTab().setText("Nguyên liệu"), 1)
//            tabLayout.addTab(tabLayout.newTab().setText("Vòng quay hàng tồn"), 2)
//            tabLayout.addTab(tabLayout.newTab().setText("Công cụ"), 3)
//            tabLayout.addTab(tabLayout.newTab().setText("Phế phẩm"), 4)
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

            tvRangeTime.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
            rlRangeTime.setOnClickListener {
                postNormal(EventNextHome(RangeTimeSelectFragment::class.java))
            }
            pullToRefresh.setOnRefreshListener {
                EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_DASHBOARD_MANUFACTURING))
                pullToRefresh.isRefreshing = false
            }
//            getDataWarehouseReport()
        }
    }

    fun getDataWarehouseReport(){
        binding?.pullToRefresh?.isRefreshing = false
        homeViewModel.getListDataReportWarehouse(
            ReportRequestModel(
                homeViewModel.fromDate,
                homeViewModel.toDate,
                if (homeViewModel.isCompare) homeViewModel.fromDateCompare else null,
                if (homeViewModel.isCompare) homeViewModel.toDateCompare else null,
            )
        )
    }
}