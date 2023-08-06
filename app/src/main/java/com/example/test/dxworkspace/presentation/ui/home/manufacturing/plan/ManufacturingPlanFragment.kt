package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanModel
import com.example.test.dxworkspace.databinding.FragmentManufacturingPlanBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.CommandAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter.PlanAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.create.CreateManufacturingPlanFragment
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.common.clearText
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject

class ManufacturingPlanFragment : BaseFragment<FragmentManufacturingPlanBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_plan
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingPlanViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    @Inject
    lateinit var dashboardViewModel : DashboardControlManufacturingViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel


    val adapter by lazy { PlanAdapter() }

    var listPlans = listOf<ManufacturingPlanModel>()
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    fun onBusEvent(event: EventUpdate) {
        when (event.type) {
            EventUpdate.UPDATE_DASHBOARD_MANUFACTURING -> {
                binding?.tvRangeTime?.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
                getListPlan()
                getProgressPlan()
            }
            EventUpdate.UPDATE_PLAN -> {
                getListPlan()
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(listPlan) {
                binding?.pullToRefresh?.isRefreshing = false
                listPlans = it ?: listPlans
                adapter.items = listPlans
            }
            observe(listComponent){
                val t = it ?: listOf()
                if(t.isEmpty() || t.find { it.name == "create-manufacturing-plan" } == null){
                    binding?.lnCreate?.isVisible = false
                }
            }
        }
        dashboardViewModel = viewModel(viewModelFactory){
            observe(numberOfPlanByProgress){
                val s = it?.slowPlans ?: 0
                val e = it?.expiredPlans ?: 0
                if( s != 0 || e != 0  ){
                    showToast(EventToast(text = "Bạn có $s kế hoạch chậm và $e kế hoạch quá hạn. Hãy thực hiện ngay nhé!"))
//                    Toast.makeText(requireContext(),"Bạn có $s kế hoạch chậm và $e kế hoạch quá hạn.\n Hãy thực hiện ngay nhé!",Toast.LENGTH_LONG).show()
                }
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getComponent()
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            pullToRefresh.setOnRefreshListener {
                getListPlan()
            }
            tvRangeTime.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
            tvRangeTime.setOnClickListener {
                postNormal(EventNextHome(RangeTimeSelectFragment::class.java))
            }
            rcvWork.adapter = adapter
            rcvWork.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            adapter.onClick = {
                val t= it
                postNormal(
                    EventNextHome(
                        ManufacturingPlanDetailFragment::class.java, bundleOf(
                            Pair("PLAN_ID",listPlans.first{it._id == t}._id)
                        )
                    )
                )
            }
            btnSave.setOnClickListener {
                postNormal(EventNextHome(CreateManufacturingPlanFragment::class.java))
            }
            layoutSearch.itemSearch.edtSearch.hint = "Mã kế hoạch sản xuất"
            search(layoutSearch.itemSearch){
                val l = search(it)
                adapter.items = l
            }
            layoutSearch.itemSearch.ivClearSearch.setOnClickListener {
                layoutSearch.itemSearch.edtSearch.clearText()
                layoutSearch.itemSearch.ivClearSearch.isVisible = false
                adapter.items = listPlans
            }
        }
        getListPlan()
        getProgressPlan()
    }


    fun getListPlan(){
        viewModel.getListPlan(homeViewModel.fromDate, homeViewModel.toDate)
    }

    fun getProgressPlan(){
        dashboardViewModel.getNumberOfPlanByProgress(
            configRepository.getCurrentRole().id,
            null,
            homeViewModel.fromDate,
            homeViewModel.toDate)
    }
    fun search(key:String) = listPlans.filter { it.code.contains(key) }
}