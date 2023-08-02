package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
import com.example.test.dxworkspace.databinding.FragmentManufacturingCommandNewBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.CommandAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkViewModel
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.common.clearText
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject

class ManufacturingCommandFragment : BaseFragment<FragmentManufacturingCommandNewBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_command_new
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingCommandViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    @Inject
    lateinit var dashboardViewModel : DashboardControlManufacturingViewModel

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
                getProgressPlan()
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
        dashboardViewModel = viewModel(viewModelFactory){
            observe(numberOfCommandByProgress){
                val s = it?.slowCommands ?: 0
                val e = it?.expiredCommands ?: 0
                if( s != 0 || e != 0  ){
                    showToast(EventToast(text = "Bạn có $s lệnh chậm và $e lệnh quá hạn. Hãy thực hiện ngay nhé!"))
//                    Toast.makeText(requireContext(),"Bạn có $s kế hoạch chậm và $e kế hoạch quá hạn.\n Hãy thực hiện ngay nhé!",Toast.LENGTH_LONG).show()
                }
            }
        }
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
            layoutSearch.itemSearch.edtSearch.hint = "Mã lệnh sản xuất"
            search(layoutSearch.itemSearch){
                val l = search(it)
                adapter.items = l
            }
            layoutSearch.itemSearch.ivClearSearch.setOnClickListener {
                layoutSearch.itemSearch.edtSearch.clearText()
                layoutSearch.itemSearch.ivClearSearch.isVisible = false
                adapter.items = listCommands
            }
        }
        getAllCommand()
        getProgressPlan()
//        homeViewModel.getAllOrganizationUnit()
//        homeViewModel.getAllRoles()
//        if(homeViewModel.listManufacturingWork.value.isNullOrEmpty()) viewModel.getManufacturingWorks() else {
//            viewModel.listWorks.value = homeViewModel.listManufacturingWork.value
//        }
    }

    fun getAllCommand(){
        viewModel.getAllCommand(homeViewModel.fromDate,homeViewModel.toDate)
    }
    fun getProgressPlan(){
        dashboardViewModel.getNumberOfCommandByProgress(
            configRepository.getCurrentRole().id,
            null,
            homeViewModel.fromDate,
            homeViewModel.toDate)
    }

    fun search(key:String) = listCommands.filter { it.code.contains(key) }

}