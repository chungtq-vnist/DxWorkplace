package com.example.test.dxworkspace.presentation.ui.home.workplace.notify

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.notify.NotificationModel
import com.example.test.dxworkspace.data.entity.notify.ParamGetPageNotify
import com.example.test.dxworkspace.data.entity.notify.ParamMarkNotifyReaded
import com.example.test.dxworkspace.databinding.FragmentNotifyBinding
import com.example.test.dxworkspace.databinding.FragmentNotifyByTypeBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.workplace.WorkplaceViewModel
import com.example.test.dxworkspace.presentation.ui.home.workplace.notify.adapter.NotifyAdapter
import com.example.test.dxworkspace.presentation.utils.AppScrollListener
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import kotlinx.android.synthetic.main.custom_toast.*
import javax.inject.Inject

class NotifyByTypeFragment : BaseFragment<FragmentNotifyByTypeBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: NotifyViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    @Inject
    lateinit var homeViewModel: HomeViewModel

    var isShowLoading = true
    var page = 1
    var limit = 50
    var totalPage = 1
    var allNotify = mutableListOf<NotificationModel>()
    val adapter by lazy { NotifyAdapter() }
    var type = 1
    override fun getLayoutId(): Int = R.layout.fragment_notify_by_type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt("TYPE") ?: 1
        viewModel = viewModel(viewModelFactory) {
            observe(statusGetNotify) {
                binding?.pullToRefresh?.isRefreshing = false
                if (it == false) {
                    showToast(EventToast(text = "Lấy danh sách thông báo thất bại"))
                }
            }
            observe(notifyResponse) {
//                allNotify = it?.docs ?: listOf()
                totalPage = it?.totalPages ?: 1
                handleData(it?.docs?.filter { it.associatedDataObject?.dataType == type } ?: listOf())
                countNotify()
            }
            observe(markReaded){
                if(it== true) countNotify()
            }
            observe(markReadedAll){
                if(it== true){
                    adapter.items.forEach { it.readed = true }
                    adapter.notifyDataSetChanged()
                    countNotify()
                }
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            pullToRefresh.setOnRefreshListener {
                resetData()
            }
            rcvNotify.addOnScrollListener(object : AppScrollListener() {
                override fun onLoadMore() {
                    loadMore()
                }
            })
            rcvNotify.adapter = adapter
            adapter.markReaded = {
                viewModel.markReaded(ParamMarkNotifyReaded(it, false))
            }
            tvMarkAsReadAll.setOnClickListener {
                viewModel.markReadedAll(ParamMarkNotifyReaded(null,true))
            }

        }
        getNotifications()
    }

    fun handleData(list: List<NotificationModel>) {
        if (page == 1) {
            adapter.items = list.toMutableList()
            allNotify = list.toMutableList()
            binding?.apply {
                if (list.isEmpty()) {
                    viewRootRcv.isVisible = false
                    viewRootEmty.isVisible = true
                } else {
                    viewRootRcv.isVisible = true
                    viewRootEmty.isVisible = false
                }
            }
        } else {
            allNotify.addAll(list)
            adapter.appendOrder(list)
        }
    }

    fun getNotifications() {
        viewModel.getNotifications(ParamGetPageNotify(page, limit), isShowLoading)
    }

    fun resetData() {
        page = 1
        totalPage = 1
        isShowLoading = true
        getNotifications()
    }

    fun loadMore() {
        isShowLoading = false
        if (page < totalPage) {
            page++
            getNotifications()
        }
    }

    fun countNotify(){
//        val t = adapter.items
//        val countTask = t.filter { it.associatedDataObject?.dataType == 1 && !it.readed }.size
//        val countAsset = t.filter { it.associatedDataObject?.dataType == 2 && !it.readed  }.size
//        val countKPI = t.filter { it.associatedDataObject?.dataType == 3 && !it.readed  }.size
//        val countProduct = t.filter { it.associatedDataObject?.dataType == 5 && !it.readed  }.size
//        val countProcedure = t.filter { it.associatedDataObject?.dataType == 6 && !it.readed  }.size
//        val total = countTask + countAsset + countKPI + countProcedure + countProduct
//        binding?.frameTotalTask?.isVisible = countTask > 0
//        binding?.tvTaskCount?.text = if(countTask < 100) countTask.toString() else "${countTask}+"
//        binding?.frameTotalAsset?.isVisible = countAsset > 0
//        binding?.tvAssetCount?.text = if(countAsset < 100) countAsset.toString() else "${countAsset}+"
//        binding?.frameTotalKPI?.isVisible = countKPI > 0
//        binding?.tvKPICount?.text = if(countKPI < 100) countKPI.toString() else "${countKPI}+"
//        binding?.frameTotalProduct?.isVisible = countProduct > 0
//        binding?.tvProductCount?.text = if(countProduct < 100) countProduct.toString() else "${countProduct}+"
//        binding?.frameTotalProcedure?.isVisible = countProcedure > 0
//        binding?.tvProcedureCount?.text = if(countProcedure < 100) countProcedure.toString() else "${countProcedure}+"
//        EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_COUNT_NOTIFY,total))
    }
}