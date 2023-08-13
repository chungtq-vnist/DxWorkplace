package com.example.test.dxworkspace.presentation.ui.home.manufacturing.request

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
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.product_request.ProductRequestManagementModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentManufacturingRequestBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.adapter.RequestManagementAdapter
import com.example.test.dxworkspace.presentation.ui.timepicker.RangeTimeSelectFragment
import com.example.test.dxworkspace.presentation.utils.common.clearText
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.common.registerGreen
import com.example.test.dxworkspace.presentation.utils.common.unregisterGreen
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_manufacturing_request.*
import kotlinx.android.synthetic.main.item_manufacturing_request.*
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class ManufacturingRequestFragment : BaseFragment<FragmentManufacturingRequestBinding>() {

    var typePage = 0 // 0 : mua hang , 1 : nhap kho , 2 xuat kho
    var allRequest = listOf<ProductRequestManagementModel>()
    var listBuy = listOf<ProductRequestManagementModel>()
    var listImport = listOf<ProductRequestManagementModel>()
    var listExport = listOf<ProductRequestManagementModel>()
    var keySearch = ""


    val adapter = RequestManagementAdapter()
    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_request
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingRequestViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    override fun onStart() {
        super.onStart()
//        registerGreen()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
//        unregisterGreen()
        EventBus.getDefault().unregister(this)
    }

//    @Subscribe
//    fun onEventUpdate(event : EventUpdate){
//
//    }

    fun onBusEvent(event: EventUpdate) {
        when (event.type) {
            EventUpdate.UPDATE_DASHBOARD_MANUFACTURING -> {
                binding?.tvRangeTime?.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
                getData()
            }
            EventUpdate.UPDATE_REQUEST -> {
                getData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory) {
            observe(failure) {
                showToast(EventToast(R.string.error_notification))
            }
            observe(listRequest) {
                binding?.pullToRefresh?.isRefreshing = false
                allRequest = it ?: allRequest
                listBuy = allRequest.filter { it.type == 1 }.sortedByDescending { it.createdAt }
                listImport = allRequest.filter { it.type == 2 }.sortedByDescending { it.createdAt }
                listExport = allRequest.filter { it.type == 3 }.sortedByDescending { it.createdAt }
                setupUI()
            }
        }

        observeLoading(homeViewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            rcvWork.adapter = adapter
            adapter.onClick = {
                postNormal(
                    EventNextHome(
                        CreateManufacturingRequestFragment::class.java,
                        bundleOf(Pair("REQUEST_ID", it._id),)
                    )
                )
            }
            pullToRefresh.setOnRefreshListener {
                getData()
            }
            tvRangeTime.text = homeViewModel.fromDate + " - " + homeViewModel.toDate
            tvRangeTime.setOnClickListener {
                postNormal(EventNextHome(RangeTimeSelectFragment::class.java))
            }
            rcvWork.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            tabLayout.addTab(tabLayout.newTab().setText("Mua hàng"), 0)
            tabLayout.addTab(tabLayout.newTab().setText("Nhập kho"), 1)
            tabLayout.addTab(tabLayout.newTab().setText("Xuất kho"), 2)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    typePage = tab?.position ?: 0
                    setupUI()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
                }

            })
            btnSave.setOnClickListener {
                postNormal(
                    EventNextHome(
                        CreateManufacturingRequestFragment::class.java,
                        bundleOf(Pair("TYPE", typePage + 1))
                    )
                )
            }
            layoutSearch.itemSearch.edtSearch.hint = "Mã đề nghị"
            search(layoutSearch.itemSearch){
                keySearch = it
                val l = search(it)
                adapter.items = l
                setNodata()
            }
            layoutSearch.itemSearch.ivClearSearch.setOnClickListener {
                layoutSearch.itemSearch.edtSearch.clearText()
                keySearch =""
                layoutSearch.itemSearch.ivClearSearch.isVisible = false
                setupUI()
            }
        }
        getData()
        homeViewModel.getAllUser()
        homeViewModel.getAllOrganizationUnit()
        homeViewModel.getStock()
        homeViewModel.getManufacturingWorksWithoutRole()
    }

    fun setupUI() {

        when (typePage) {
            0 -> {
                adapter.items = search(keySearch)
            }
            1 -> {
                adapter.items = search(keySearch)
            }
            2 -> {
                adapter.items = search(keySearch)
            }
        }
        setNodata()
    }

    fun setNodata() {
        if (adapter.items.size == 0) {
            binding?.rcvWork?.isVisible = false
            binding?.lnHeader?.isVisible = false
            tvNodata.isVisible = true
        } else {
            binding?.rcvWork?.isVisible = true
            binding?.lnHeader?.isVisible = true
            tvNodata.isVisible = false
        }
    }

    fun getData() {
        viewModel.getProductRequest(homeViewModel.fromDate, homeViewModel.toDate)
    }

    fun search(key: String) : List<ProductRequestManagementModel> {
       return when (typePage) {
            0 -> {
                listBuy.filter {
                    it.code.contains(key)
                }
            }
            1 -> {
                listImport.filter {
                    it.code.contains(key)
                }
            }
            2 -> {
                listExport.filter {
                    it.code.contains(key)
                }
            }
           else -> listOf()
        }
    }
}