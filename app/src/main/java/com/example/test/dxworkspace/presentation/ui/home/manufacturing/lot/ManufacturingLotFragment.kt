package com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot

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
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotModel
import com.example.test.dxworkspace.databinding.FragmentManufacturingLotBinding
import com.example.test.dxworkspace.databinding.FragmentManufacturingLotDetailBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.CommandAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.adapter.LotAdapter
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import javax.inject.Inject

class ManufacturingLotFragment : BaseFragment<FragmentManufacturingLotBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_lot
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingLotViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    val adapter by lazy { LotAdapter() }
    var listLots = listOf<ManufacturingLotModel>()

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
                getAllLot()
            }
            EventUpdate.UPDATE_LOT -> {
                getAllLot()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory){
            observe(listLot){
                binding?.pullToRefresh?.isRefreshing = false
                listLots= it ?:listLots
                adapter.items = listLots
            }
        }
        observeLoading(viewModel)
        observeLoading(homeViewModel)
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
                getAllLot()
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
                        ManufacturingLotDetailFragment::class.java, bundleOf(
                            Pair("LOT_ID", t)
                        )
                    )
                )
            }
            getAllLot()
            homeViewModel.getAllUser()
            homeViewModel.getStock()
        }
    }

    fun getAllLot(){
        viewModel.getAllLot(homeViewModel.fromDate,homeViewModel.toDate)
    }



}