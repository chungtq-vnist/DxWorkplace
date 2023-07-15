package com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ManufacturingPlanDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ParamApprover
import com.example.test.dxworkspace.data.entity.manufacturing_plan.ParamUpdatePlan
import com.example.test.dxworkspace.data.entity.product_request.ParamUpdateRequest
import com.example.test.dxworkspace.databinding.FragmentManufacturingPlanBinding
import com.example.test.dxworkspace.databinding.FragmentManufacturingPlanDetailBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.ManufacturingCommandDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter.CommandInPlanDetailAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.adapter.ProductInPlanDetailAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.BottomDialogOptionRequest
import com.example.test.dxworkspace.presentation.utils.common.addDefaultDecorator
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getddMMYYYY
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class ManufacturingPlanDetailFragment : BaseFragment<FragmentManufacturingPlanDetailBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_plan_detail
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingPlanViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    @Inject
    lateinit var configRepository: ConfigRepository

    val productAdapter by lazy { ProductInPlanDetailAdapter() }
    val commandAdapter by lazy { CommandInPlanDetailAdapter() }
    var planId = ""
    var planDetail = ManufacturingPlanDetailModel()
    var dialog : BottomDialogOptionPlan? = null
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
            EventUpdate.UPDATE_PLAN -> {
//                getListPlan()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        planId = arguments?.getString("PLAN_ID") ?: ""
        viewModel= viewModel(viewModelFactory){
            observe(planDetail){
                this@ManufacturingPlanDetailFragment.planDetail = it ?: ManufacturingPlanDetailModel()
                setData()
            }
            observe(failure){
                showToast(EventToast(isFail = true, text = "Có lỗi xảy ra!"))
            }
            observe(updateStatus){
                if(it == false) showToast(EventToast(text ="Update thất bại"))
                else {
                    showToast(EventToast(isFail = false , text ="Update thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_PLAN))
                    onBackPress()
                }
            }
        }
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(planId.isNotEmpty()) viewModel.getPlanById(planId)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }

        }
    }

    fun setData(){
        binding?.apply {
            edtPlanCode.setText(planDetail.code)
            if(planDetail.salesOrders.isNullOrEmpty()){
                tilOrderCode.isVisible = false
            } else {
                edtOrderCode.setText(planDetail.salesOrders?.map { it.code }?.joinToString(", "))
            }
            edtWork.setText(planDetail.manufacturingWorks?.map { it.name }?.joinToString(", "))
            edtCreator.setText(planDetail.creator?.name)
            edtApprover.setText(planDetail.approvers?.map{it.approver?.name}?.joinToString(", "))
            when(planDetail.status){
                1 -> {
                    edtStatus.setText("Chờ phê duyệt")
                    edtStatus.setTextColorz(R.color.clr_status_wait)
                }
                2 -> {
                    edtStatus.setText("Đã phê duyệt")
                    edtStatus.setTextColorz(R.color.clr_status_approve)
                }
                3 -> {
                    edtStatus.setText("Đang thực hiện")
                    edtStatus.setTextColorz(R.color.clr_status_inprogress)
                }
                4 -> {
                    edtStatus.setText("Đã hoàn thành")
                    edtStatus.setTextColorz(R.color.clr_status_finish)
                }
                5 -> {
                    edtStatus.setText("Đã hủy")
                    edtStatus.setTextColorz(R.color.clr_status_cancel)
                }
                else -> {
                    edtStatus.setText("Error")
                    edtStatus.setTextColorz(R.color.clr_status_cancel)
                }
            }
            edtTimeCreate.setText(getddMMYYYY(planDetail.createdAt))
            edtTimeStart.setText(getddMMYYYY(planDetail.startDate))
            edtTimeEnd.setText(getddMMYYYY(planDetail.endDate))
            edtDes.setText(planDetail.description)
            rcvProduct.adapter = productAdapter
//            rcvProduct.addDefaultDecorator()
            productAdapter.items = planDetail.goods ?: listOf()
            rcvCommand.adapter = commandAdapter
            commandAdapter.items = planDetail.manufacturingCommands ?: listOf()
            commandAdapter.onClick = {
                val t = planDetail.manufacturingCommands?.get(it)
                // go to command detail
                postNormal(EventNextHome(ManufacturingCommandDetailFragment::class.java, bundleOf(Pair("COMMAND_ID",t?._id))))
            }
            btnMenuMore.setOnClickListener {
                if(planDetail.status != 4 && planDetail.status != 5 &&
                    planDetail.approvers?.map { it.approver?._id }?.contains(configRepository.getUser().id) == true)  {
                    showDialogOptionPlan()
                } else return@setOnClickListener
            }
        }
    }

    fun showDialogOptionPlan(){
        if (dialog != null || dialog?.showsDialog == true) {
            dialog?.dismiss()
            dialog = null
        }
        dialog = BottomDialogOptionPlan(planDetail, configRepository)
        dialog?.onConfirm = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận phê duyệt kế hoạch?")
                .setMessage("Chú ý: Bạn đang chuẩn bị phê duyệt kế hoạch sản xuất. Hành động này sẽ xác nhận và đưa kế hoạch vào giai đoạn thực hiện. Bạn có chắc chắn muốn tiếp tục?")
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    viewModel.updatePlan(planId, ParamUpdatePlan(ParamApprover(configRepository.getUser().id),null))
                    dialog.dismiss()
                }
                .show()
        }
        dialog?.onCancel = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận hủy kế hoạch?       ")
                .setMessage("Chú ý: Bạn đang chuẩn bị hủy kế hoạch sản xuất. Thao tác này sẽ gây ảnh hưởng đến lịch trình sản xuất hiện tại. Bạn có chắc chắn muốn tiếp tục?")
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    viewModel.updatePlan(planId, ParamUpdatePlan(null,5))
                    dialog.dismiss()
                }
                .show()
        }
        dialog?.show(childFragmentManager,"BottomDialogOptionRequest")

    }


}