package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.databinding.FragmentDashboardControlManufacturingBinding
import com.example.test.dxworkspace.presentation.model.menu.ManufacturingWorkSelect
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog.DialogSelectManufacturingWork
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog.RangeDateSelectFragment
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

class DashboardControlManufacturingFragment : BaseFragment<FragmentDashboardControlManufacturingBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard_control_manufacturing
    }

    var dialog: DialogSelectManufacturingWork? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: DashboardControlManufacturingViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    lateinit var dashboardControPageAdapter: DashboardControlPagerAdapter


    override fun onStart() {
        super.onStart()
        registerGreen()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        unregisterGreen()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onEventUpdate(event : EventUpdate){

    }

    fun onBusEvent(event : EventUpdate){
        when(event.type){
            EventUpdate.UPDATE_DASHBOARD_MANUFACTURING -> {
                binding?.tvRangeTime?.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            initViewPager()
            ivBack.setOnClickListener { onBackPress() }
            viewpager.adapter = dashboardControPageAdapter
            viewpager.isUserInputEnabled = false
            tabLayout.addTab(tabLayout.newTab().setText("Kế hoạch sản xuất"), 0)
            tabLayout.addTab(tabLayout.newTab().setText("Lệnh sản xuất"), 1)
            tabLayout.addTab(tabLayout.newTab().setText("Đề nghị"), 2)
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
            viewModel.getAllManufacturingWorkCanManage()
            btnMenuMore.setOnClickListener {
                showDialogSelectWorks(if (viewModel.listWorksSelected.isEmpty()) viewModel.listAllWorks else viewModel.listWorksSelected)
            }
            tvRangeTime.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
            rlRangeTime.setOnClickListener {
                postNormal(EventNextHome(RangeDateSelectFragment::class.java))
            }
        }
    }

    fun initViewPager() {
        dashboardControPageAdapter = DashboardControlPagerAdapter(childFragmentManager, lifecycle)
    }

    private fun showDialogSelectWorks(items: MutableList<ManufacturingWorkSelect>) {
        if (dialog != null || dialog?.showsDialog == true) {
            dialog?.dismiss() // Hủy dialog cũ
            dialog = null // Gán giá trị null cho biến dialog
        }
        dialog = DialogSelectManufacturingWork()
        dialog?.onApplyWorks = { list ->
            viewModel.listWorksSelected = list
            homeViewModel.listWorksSelected = list
            EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_LISTWORK_DASHBOARD_MANUFACTURING))
        }
        dialog?.data = items.map { e ->
            val t = ManufacturingWorkSelect(e.id, e.name)
            t.isSelected = e.isSelected
            t

        }.toMutableList()
        dialog?.show(childFragmentManager, "dialog2")

    }
}