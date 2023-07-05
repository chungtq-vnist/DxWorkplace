package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.databinding.FragmentDashboardManufacturingCommandBinding
import com.example.test.dxworkspace.databinding.FragmentManufacturingCommandBinding
import com.example.test.dxworkspace.databinding.FragmentManufacturingCommandDetailBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.CommandAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkViewModel
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject

class ManufacturingCommandFragment : BaseFragment<FragmentManufacturingCommandBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_command
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingCommandViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    val adapter by lazy { CommandAdapter() }
    var listCommands = listOf<ManufacturingCommandModel>()

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
                getAllCommand()
            }
            EventUpdate.UPDATE_COMMAND -> {
                getAllCommand()
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(listCommand) {
                binding?.pullToRefresh?.isRefreshing = false
                listCommands = it ?:listCommands
                adapter.items = listCommands
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            tvRangeTime.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
            tvRangeTime.setOnClickListener {
                postNormal(EventNextHome(RangeTimeSelectFragment::class.java))
            }
            pullToRefresh.setOnRefreshListener {
                getAllCommand()
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
                        ManufacturingCommandDetailFragment::class.java, bundleOf(
                            Pair("COMMAND_ID", t)
                        )
                    )
                )
            }
        }
        getAllCommand()
//        homeViewModel.getAllOrganizationUnit()
//        homeViewModel.getAllRoles()
//        if(homeViewModel.listManufacturingWork.value.isNullOrEmpty()) viewModel.getManufacturingWorks() else {
//            viewModel.listWorks.value = homeViewModel.listManufacturingWork.value
//        }
    }

    fun getAllCommand(){
        viewModel.getAllCommand(homeViewModel.fromDate,homeViewModel.toDate)
    }
}