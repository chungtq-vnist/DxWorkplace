package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.bill.BillByCommandModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.databinding.DialogOptionTaskBinding
import com.example.test.dxworkspace.databinding.FragmentManufacturingCommandDetailBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.RequestApproveCommand
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.*
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill.ManufacturingMillViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.CreateManufacturingRequestFragment
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.common.registerGreen
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz
import com.example.test.dxworkspace.presentation.utils.common.unregisterGreen
import com.example.test.dxworkspace.presentation.utils.convertToDate
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getddMMYYYY
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class ManufacturingCommandDetailFragment : BaseFragment<FragmentManufacturingCommandDetailBinding>() {
    var commandId = ""
    var command = ManufacturingCommandModel()
    var listBills = listOf<BillByCommandModel>()
    var listInventorys = listOf<InventoryGoodWrap>()
    var dialog : BottomDialogOptionCommand? = null
    var dialogCheckQuality : DialogCheckQuality? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_command_detail
    }

    val adapterManagerMaterial by lazy { ListEmployeeAdapter() }
    val adapterPerformer by lazy { ListEmployeeAdapter() }
    val adapterOperator by lazy { ListEmployeeAdapter() }
    val infoMaterialAdapter by lazy { InfoMaterialAdapter() }
    val itemBillExport by lazy { InfoRequestMaterialExportAdapter() }
    val qualityAdapter by lazy { InfoQualityAdapter() }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingCommandViewModel

    @Inject
    lateinit var config : ConfigRepository

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
            EventUpdate.UPDATE_COMMAND -> {
                viewModel.getManufacturingCommandById(commandId)
                viewModel.getBillByCommand(commandId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commandId = arguments?.getString("COMMAND_ID") ?: ""
        viewModel = viewModel(viewModelFactory){
            observe(commandDetail){
                command = it ?: command
                setupData()
                if(command.exportMaterialRequest?.isNotEmpty() == true) {
                    viewModel.getRequestExportMaterialOfCommand(command.exportMaterialRequest!!)
                    binding?.apply {
                        view3.isVisible = true
                        tvTitleBillExportMaterial.isVisible = true
                        rcvBillExportMaterial.isVisible = true
                    }
                } else {
                    binding?.apply {
                        view3.isVisible = false
                        tvTitleBillExportMaterial.isVisible = false
                        rcvBillExportMaterial.isVisible = false
                    }
                }
                viewModel.getInventoryByGoods(command.good.materials?.filter { !it.good?._id.isNullOrEmpty() }?.map { it.good!!._id } ?: listOf())
            }
//            observe(listBill){
//                listBills = it ?: listBills
//                binding?.apply {
//                    if(listBills.isEmpty()){
//                        rcvBillExportMaterial.isVisible = false
//                        tilStock.isVisible = false
//                        tilBillStatus.isVisible = false
//                        tvNoBill.isVisible = true
//                    } else {
//                        val t = listBills.first()
//                        rcvBillExportMaterial.isVisible = true
//                        tilStock.isVisible = true
//                        tvNoBill.isVisible = false
//                        tilBillStatus.isVisible = true
//                        edtStock.setText(t.fromStock?.name)
//                        edtBillStatus.setText(
//                            when(t.status){
//                                "1"-> "Chờ phê duyệt"
//                                "2"-> "Chờ thực hiện"
//                                "3"-> "Đang thực hiện"
//                                "4"-> "Chờ phê duyệt"
//                                "5"-> "Đã hoàn thành"
//                                "7"-> "Đã hủy phiếu"
//                                else -> "Chờ phê duyệt"
//                            }
//                        )
//                        rcvBillExportMaterial.adapter = itemBillExport
//                        itemBillExport.items = t.goods ?: listOf()
//                    }
//                }
//            }
            observe(listRequest){
                binding?.apply {
                    rcvBillExportMaterial.adapter = itemBillExport
                    itemBillExport.items = it ?: listOf()
                }
            }
            observe(listInventory){
                listInventorys = it ?: listOf()
                justTry {
                    infoMaterialAdapter.listInventory = listInventorys
                    infoMaterialAdapter.notifyDataSetChanged()
                }
            }
            observe(statusCancelCommand){
                if(it == false ) showToast(EventToast(text ="Hủy lệnh thất bại"))
                else {
                    showToast(EventToast(isFail = false, text ="Hủy lệnh thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_COMMAND))
                    onBackPress()
                }
            }
            observe(statusStartCommand){
                if(it == false ) showToast(EventToast(text ="Bắt đầu lệnh thất bại"))
                else {
                    showToast(EventToast(isFail = false, text ="Bắt đầu lệnh thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_COMMAND))
                    onBackPress()
                }
            }
            observe(statusUpdate){
                if(it == false ) showToast(EventToast(text ="Update thất bại"))
                else {
                    showToast(EventToast(isFail = false, text ="Update thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_COMMAND))
                    onBackPress()
                }
            }
        }
        observeLoading(viewModel)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            btnMenuMore.setOnClickListener {
                showDialog()
            }
            rcvMaterialManager.adapter = adapterManagerMaterial
            rcvPerformer.adapter = adapterPerformer
            rcvOperator.adapter = adapterOperator
            rcvMaterial.adapter = infoMaterialAdapter
            constraintManagerMaterial.setOnClickListener {
                rcvMaterialManager.isVisible = !rcvMaterialManager.isVisible
                ivExpandResponsible.setImageResource(if(rcvMaterialManager.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)
            }
            constraintPerformer.setOnClickListener {
                rcvPerformer.isVisible = !rcvPerformer.isVisible
                ivExpandPerformer.setImageResource(if(rcvPerformer.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)
            }
            constraintOperator.setOnClickListener {
                rcvOperator.isVisible = !rcvOperator.isVisible
                ivExpandOperator.setImageResource(if(rcvOperator.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)
            }
        }
        viewModel.getManufacturingCommandById(commandId)
//        viewModel.getBillByCommand(commandId)
        disableEdit()
    }

    fun disableEdit(){
        binding?.apply {

            edtCommandCode.isEnabled = false
            edtPlanCode.isEnabled = false
            edtOrderCode.isEnabled = false
            edtLotCode.isEnabled = false
            edtMillName.isEnabled = false
            edtTimeStart.isEnabled = false
            edtTimeEnd.isEnabled = false
            edtCreatedAt.isEnabled = false
            edtStatus.isEnabled = false
            edtProductCode.isEnabled = false
            edtProductName.isEnabled = false
            edtUnit.isEnabled = false
            edtProductQuantity.isEnabled = false
            edtTimeFinished.isEnabled = false
            edtProductPercent.isEnabled = false
            edtWastePercent.isEnabled = false
            edtWasteQuantity.isEnabled = false
        }
    }

    fun setupData(){
        binding?.apply {


            edtCommandCode.setText(command.code)
            edtPlanCode.setText(command.manufacturingPlan?.code)
            edtOrderCode.setText(command.manufacturingPlan?.salesOrders?.map { it.code }?.joinToString())
            edtLotCode.setText(command.lot?.map { it.code }?.joinToString())
            tilLotCode.isVisible = !command.lot.isNullOrEmpty()
            tilOrderCode.isVisible = !(command.manufacturingPlan?.salesOrders?.isNullOrEmpty() ?: true)
            edtMillName.setText(command.manufacturingMill?.code)
            edtTimeStart.setText(getddMMYYYY(command.startDate))
            edtTimeEnd.setText(getddMMYYYY(command.endDate))
            edtCreatedAt.setText(getddMMYYYY(command.createdAt))
            edtStatus.setText(
                when(command.status){
                    1 -> "Chờ phê duyệt"
                    2 -> "Lệnh đã duyệt"
                    3 -> "Đang thực thi"
                    4 -> "Đã hoàn thành"
                    5 -> "Đã hủy"
                    6 -> "Mới tạo"
                    else -> ""
                }
            )
            edtStatus.setTextColorz(
                when(command.status){
                    1 -> R.color.clr_status_wait
                    2 -> R.color.clr_status_approve
                    3 -> R.color.clr_status_inprogress
                    4 -> R.color.clr_status_finish
                    5 -> R.color.clr_status_cancel
                    6 -> R.color.clr_status_wait
                    else -> R.color.clr_status_wait
                }
            )
            edtProductCode.setText(command.good.code)
            edtProductName.setText(command.good.name)
            edtUnit.setText(command.good.baseUnit)
            edtQuantity.setText(command.quantity.toString())

            adapterManagerMaterial.items = command.approvers?.filter { !it.approver?._id.isNullOrEmpty() }?.map { it.approver!! } ?: listOf()
            adapterPerformer.items = command.responsibles ?: listOf()
            adapterOperator.items = command.accountables ?: listOf()
            infoMaterialAdapter.quantity = command.quantity
            infoMaterialAdapter.listInventory = listInventorys
            infoMaterialAdapter.items = command.good.materials ?: listOf()
            rcvQualityVerify.adapter = qualityAdapter
            qualityAdapter.items = command.qualityControlStaffs ?: listOf()
            if(command.status == 4) {
                view4.isVisible = true
                tvResult.isVisible = true
                tilTimeFinished.isVisible = true
                tilProductQuantity.isVisible = true
                tilProductPercent.isVisible = true
                tilWasteQuantity.isVisible = true
                tilWastePercent.isVisible = true
                edtTimeFinished.setText(getddMMYYYY(command.finishedTime ?:""))
                edtProductQuantity.setText(command.finishedProductQuantity.toString())
                edtWasteQuantity.setText((command.substandardProductQuantity ?: 0).toString())
                edtProductPercent.setText(((((command.finishedProductQuantity?: 0)/(command.quantity.toDouble()))*10000).toLong()/100).toString() + "%")
                edtWastePercent.setText(((((command.substandardProductQuantity?: 0)/(command.quantity.toDouble()))*10000).toLong()/100).toString() + "%")
            }


        }
    }

    fun showDialog(){
        if(dialog != null || dialog?.showsDialog == true){
            dialog?.dismiss()
            dialog= null
        }
        dialog = BottomDialogOptionCommand(command,listBills,config)
        dialog?.onStart = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Bắt đầu thực hiện lệnh?        ")
                .setMessage("Chú ý: Bạn đang chuẩn bị bắt đầu thực hiện lệnh sản xuất. Bạn có chắc chắn muốn tiếp tục?")
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    // Respond to neutral button press
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    // Respond to positive button press
                    viewModel.startCommand(RequestApproveCommand(status = 3),commandId)
                    dialog.dismiss()
                }
                .show()
        }
        dialog?.onCancel = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận hủy lệnh?           ")
                .setMessage("Chú ý: Bạn đang chuẩn bị hủy lệnh sản xuất. Thao tác này sẽ không thể hoàn tác. Bạn có chắc chắn muốn tiếp tục?")
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    viewModel.cancelCommand(RequestApproveCommand(status = 5),commandId)
                    dialog.dismiss()
                }
                .show()
        }
        dialog?.onExport = {
//            postNormal(EventNextHome(ExportMaterialFragment::class.java, bundleOf(Pair("QUANTITY",command.quantity),Pair("COMMAND",command),
            postNormal(EventNextHome(ExportMaterialFragmentNew::class.java, bundleOf(Pair("QUANTITY",command.quantity),Pair("COMMAND",command),
            Pair("INVENTORY",gson.toJson(listInventorys)))))
        }
        dialog?.onBuyGood = {
            postNormal(EventNextHome(CreateManufacturingRequestFragment::class.java,
            bundleOf(Pair("TYPE",1),Pair("COMMAND",command),Pair("FROM","BUY_IN_COMMAND"),
            Pair("INVENTORY",listInventorys)
                )))
        }
        dialog?.onCheckQuality = {
            showDialogCheckQuality()
        }
        dialog?.onFinish = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận hoàn thành lệnh?         ")
                .setMessage("Chú ý: Bạn đang chuẩn bị hoàn thành lệnh sản xuất. Hành động này sẽ không thể chỉnh sửa sau này. Bạn có chắc chắn muốn tiếp tục?")
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    postNormal(EventNextHome(CreateLotFragment::class.java, bundleOf(Pair("COMMAND",command))))
                    dialog.dismiss()
                }
                .show()
        }
        dialog?.show(childFragmentManager,"BottomDialogOptionCommand")
    }

    fun showDialogCheckQuality(){
        if(dialogCheckQuality != null || dialogCheckQuality?.showsDialog == true){
            dialogCheckQuality?.dismiss()
            dialogCheckQuality= null
        }
        dialogCheckQuality = DialogCheckQuality(command,config.getUser().id)
        dialogCheckQuality?.onAccept = {
            viewModel.updateQualityControlCommand(it,commandId)
            dialog?.dismiss()
        }
        dialogCheckQuality?.show(childFragmentManager,"dialogCheckQuality")
    }


}