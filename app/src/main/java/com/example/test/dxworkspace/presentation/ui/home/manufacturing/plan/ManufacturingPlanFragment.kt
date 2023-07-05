package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanModel
import com.example.test.dxworkspace.databinding.FragmentManufacturingPlanBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.CommandAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter.PlanAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.create.CreateManufacturingPlanFragment
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
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
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                            Pair("PLAN",listPlans.first{it._id == t})
                        )
                    )
                )
            }
            btnSave.setOnClickListener {
                postNormal(EventNextHome(CreateManufacturingPlanFragment::class.java))
            }
        }
        getListPlan()

    }


    fun getListPlan(){
        viewModel.getListPlan(homeViewModel.fromDate, homeViewModel.toDate)
    }
}