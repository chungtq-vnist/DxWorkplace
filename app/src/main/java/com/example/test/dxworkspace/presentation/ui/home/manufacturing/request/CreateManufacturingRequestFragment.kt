package com.example.test.dxworkspace.presentation.ui.home.manufacturing.request

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.good.GoodDetailModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.product_request.*
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentCreateRequestBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.RequestApproveCommand
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.BottomDialogOptionCommand
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.InfoMaterialAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.adapter.ItemRequestAdapter
import com.example.test.dxworkspace.presentation.utils.common.generateCode
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz
import com.example.test.dxworkspace.presentation.utils.convertToDate
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getddMMYYYY
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CreateManufacturingRequestFragment : BaseFragment<FragmentCreateRequestBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_create_request
    }

    var requestId = ""
    var from: String = ""
    var command = ManufacturingCommandModel()
    var listInventory = listOf<InventoryGoodWrap>()
    val infoAdapter = InfoMaterialAdapter()
    var requestDetail = ProductRequestManagementModel()
    var dialog : BottomDialogOptionRequest? = null

    val calendar = Calendar.getInstance()
    var request = ParamCreateProductRequest()
    var adapter = ItemRequestAdapter()
    var allUser = listOf<UserProfileResponse>()
    var allUnit = listOf<OrganizationUnit>()
    var allStock = listOf<StockModel>()
    var allWork = listOf<ManufacturingWorkModel>()
    var goodNow = GoodDetailModel()
    var listGood = mutableListOf<GoodDetailModel>()
    var type = 0

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingRequestViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var config : ConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt("TYPE") ?: 1
        from = arguments?.getString("FROM") ?: ""
        command = arguments?.getParcelable("COMMAND") ?: command
        requestId = arguments?.getString("REQUEST_ID") ?: ""
        listInventory = arguments?.getParcelableArrayList<InventoryGoodWrap>("INVENTORY") ?: listOf()
        viewModel = viewModel(viewModelFactory) {
            observe(listGoods) {
                binding?.apply {
                    edtProduct.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            it!!
                        )
                    )
                    edtProduct.setOnItemClickListener { adapterView, view, i, l ->
                        val t = (adapterView.getItemAtPosition(i) as GoodDetailModel)
                        goodNow = t
                        edtBaseUnit.setText(t.baseUnit)
                    }
                }
            }
            observe(statusUpdate) {
                if (it == false) showToast(EventToast(text = "Update thất bại"))
                else {
                    showToast(EventToast(isFail = false, text = "Update thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_REQUEST))
                    onBackPress()
                }
            }
            observe(detailRequest){
                requestDetail = it ?: requestDetail
                setupDetail()
            }
        }
        observe(homeViewModel.listUser) {
            allUser = it ?: allUser
        }
        observe(homeViewModel.listOrganization) {
            allUnit = it ?: allUnit
            binding?.apply {
                edtUnit.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        allUnit
                    )
                )
                edtUnit.setOnItemClickListener { adapterView, view, i, l ->
                    val t = (adapterView.getItemAtPosition(i) as OrganizationUnit)
                    request.orderUnit = t._id
                    request.approvers?.removeIf { it.approveType == 2 }
                    edtBuyApprover.setText("",false)
                    edtBuyApprover.setAdapter(
                        ArrayAdapter(
                            requireContext(), android.R.layout.simple_dropdown_item_1line,
                            (t.managers?.first()?.users?.map { it.userId })?.toMutableList()
                                ?: mutableListOf()
                        )
                    )
                }
            }
        }
        observe(homeViewModel.listStock) {
            allStock = it ?: allStock
            binding?.apply {
                edtStock.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        allStock
                    )
                )
                edtStock.setOnItemClickListener { adapterView, view, i, l ->
                    val t = (adapterView.getItemAtPosition(i) as StockModel)
                    request.stock = t._id
                }
            }
        }
        observe(homeViewModel.listManufacturingWork) {
            allWork = it ?: allWork
            if (command._id.isNotEmpty()) allWork = allWork.filter {
                it.manufacturingMills?.map { it._id }
                    ?.contains(command.manufacturingMill?._id) == true
            }
            binding?.apply {
                edtWork.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        allWork
                    )
                )
                edtWork.setOnItemClickListener { adapterView, view, i, l ->
                    val t = (adapterView.getItemAtPosition(i) as ManufacturingWorkModel)
                    request.manufacturingWork = t._id
                    request.approvers?.removeIf { it.approveType == 1 }
                    edtWorkApprover.setText("",false)
                    edtWorkApprover.setAdapter(
                        ArrayAdapter(
                            requireContext(), android.R.layout.simple_dropdown_item_1line,
                            (t.organizationalUnit?.managers?.first()?.users?.map { it.userId })?.toMutableList()
                                ?: mutableListOf()
                        )
                    )
                }
            }

        }
        observeLoading(homeViewModel)
        observeLoading(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(requestId.isNotEmpty()) viewModel.getRequestDetail(requestId)
        allUser = homeViewModel.listUser.value ?: listOf()
        allStock = homeViewModel.listStock.value ?: listOf()
        allUnit = homeViewModel.listOrganization.value ?: listOf()
        allWork = homeViewModel.listManufacturingWork.value ?: listOf()
        binding?.apply {
            constraintInfoMaterial.isVisible = command._id.isNotEmpty()
            rcvMaterial.adapter = infoAdapter
            infoAdapter.quantity = command.quantity
            infoAdapter.listInventory = listInventory
            infoAdapter.items = command.good.materials ?: listOf()
            tvHeaderToolbar.text = if(type == 1) "Tạo đề nghị mua hàng" else if(type ==2) "Tạo đề nghị nhập kho" else "Tạo đề nghị xuất kho"
        }
        if (type == 1) {
            request.code = generateCode("PCR")
            request.status = 1
            request.requestType = 1
            request.type = type
            binding?.tilUnit?.isVisible = true
            binding?.tilBuyApprover?.isVisible = true
        } else if (type == 2) {
            request.code = generateCode("GRR")
            request.status = 1
            request.requestType = 1
            request.type = type
            binding?.tilUnit?.isVisible = false
            binding?.tilBuyApprover?.isVisible = false
        } else {
            request.code = generateCode("GIR")
            request.status = 1
            request.requestType = 1
            request.type = type
            binding?.tilUnit?.isVisible = false
            binding?.tilBuyApprover?.isVisible = false
        }
        binding?.apply {
            edtCode.setText(request.code)
            rcvCreate.adapter = adapter
            edtTime.setOnClickListener {
                showDateTimePickerDialog()
            }
            edtStock.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    allStock
                )
            )
            edtStock.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as StockModel)
                request.stock = t._id
            }
            edtWork.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, allWork)
            )
            edtWork.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as ManufacturingWorkModel)
                request.manufacturingWork = t._id
                request.approvers?.removeIf { it.approveType == 1 }
                edtWorkApprover.setAdapter(
                    ArrayAdapter(
                        requireContext(), android.R.layout.simple_dropdown_item_1line,
                        (t.organizationalUnit?.managers?.first()?.users?.map { it.userId })?.toMutableList()
                            ?: mutableListOf()
                    )
                )
                edtWorkApprover.setText("",false)
            }
            edtWorkApprover.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as UserProfileResponse)
                request.approvers?.removeIf { it.approveType == 1 }
                request.approvers?.add(
                    ParamInformation(
                        mutableListOf(ParamApprover(null, t.id)),
                        1
                    )
                )
            }
            edtUnit.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    allUnit
                )
            )
            edtUnit.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as OrganizationUnit)
                request.orderUnit = t._id
                request.approvers?.removeIf { it.approveType == 2 }
                edtBuyApprover.setAdapter(
                    ArrayAdapter(
                        requireContext(), android.R.layout.simple_dropdown_item_1line,
                        (t.managers?.first()?.users?.map { it.userId })?.toMutableList()
                            ?: mutableListOf()
                    )
                )
                edtBuyApprover.setText("",false)
            }
            edtBuyApprover.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as UserProfileResponse)
                request.approvers?.removeIf { it.approveType == 2 }
                request.approvers?.add(
                    ParamInformation(
                        mutableListOf(ParamApprover(null, t.id)),
                        2
                    )
                )
            }
            edtProductType.setAdapter(
                ArrayAdapter(
                    requireContext(), android.R.layout.simple_dropdown_item_1line,
                    listOf("Nguyên vật liệu", "Dụng cụ", "Thành phẩm", "Phế phẩm")
                )
            )
            edtProductType.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as String)
                when (t) {
                    "Nguyên vật liệu" -> {
                        viewModel.getGoodByType("material")
                    }
                    "Dụng cụ" -> {
                        viewModel.getGoodByType("equipment")
                    }
                    "Thành phẩm" -> {
                        viewModel.getGoodByType("product")
                    }
                    "Phế phẩm" -> {
                        viewModel.getGoodByType("waste")
                    }
                }
            }
            btnAdd.setOnClickListener {
                if (goodNow._id.isNotEmpty() && edtQuantity.getTextz().isNotEmpty()) {
                    goodNow.quantity = edtQuantity.getTextz().toLong()
                    val t = listGood.find { it._id == goodNow._id }
                    if(t == null) {
                        listGood.add(goodNow)
                    } else {
                        t.quantity +=goodNow.quantity
                    }
                    goodNow = GoodDetailModel()
                    edtProduct.setText("",false)
                    edtBaseUnit.setText("")
                    edtQuantity.setText("")
                    adapter.items = listGood
                }
            }
            btnSave.setOnClickListener {
                if(requestId.isEmpty()) {
                    if (listGood.isEmpty() || request.stock.isEmpty() || request.approvers.isNullOrEmpty()) {
                        showToast(EventToast(text = "Vui lòng nhập đủ thông tin"))
                        return@setOnClickListener
                    }
                    request.goods =
                        listGood.map { ParamGood(it._id, it.quantity.toString()) }.toMutableList()
                    println(request.toString())
                    if (from == "BUY_IN_COMMAND") request.commandId = command._id
                    viewModel.createProductRequest(request)
                } else {
                    if (listGood.isEmpty() || (request.manufacturingWork.isNotEmpty() && request.approvers?.find { it.approveType ==1 } == null)
                        || (!request.orderUnit.isNullOrEmpty() && request.approvers?.find { it.approveType ==2 } == null)) {
                        showToast(EventToast(text = "Vui lòng nhập đủ thông tin"))
                        return@setOnClickListener
                    }
                    request.goods =
                        listGood.map { ParamGood(it._id, it.quantity.toString()) }.toMutableList()
                    viewModel.updateProductRequest(request,requestDetail._id)
                }
            }
            btnMenuMore.setOnClickListener {
                showDialog()
            }

        }
        adapter.onDelete = {
            listGood.removeAt(it)
            adapter.notifyDataSetChanged()
        }
        adapter.onEdit = {
            goodNow = listGood[it]
            listGood.removeAt(it)
            adapter.notifyDataSetChanged()
            binding?.apply {
                edtQuantity.setText(goodNow.quantity.toString())
                edtBaseUnit.setText(goodNow.baseUnit)
                edtProduct.setText(goodNow.toString())
            }
        }

        if(from.isNotEmpty()){
            homeViewModel.getAllUser()
            homeViewModel.getAllOrganizationUnit()
            homeViewModel.getStock()
            homeViewModel.getManufacturingWorksWithoutRole()
        }

    }


    private fun showDateTimePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                binding?.edtTime?.setText(
                    SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)
                )
                request.desiredTime =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
                        calendar.time
                    )

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun setupDetail(){
        val isEdit = requestDetail.status == 1 && (
                requestDetail.approvers?.map { it.information?.firstOrNull()?.approver }?.find { it?._id == config.getUser().id } != null
                        || requestDetail.creator?._id == config.getUser().id)
                        // them dieu kien use vao
        request = ParamCreateProductRequest()
        binding?.apply {
            tvHeaderToolbar.text = if(requestDetail.type == 1) "Chi tiết đề nghị mua hàng" else if(requestDetail.type ==2) "Chi tiết đề nghị nhập kho" else "Chi tiết đề nghị xuất kho"
            edtCode.setText(requestDetail.code)
            edtTime.setText(getddMMYYYY(requestDetail.desiredTime ?: ""))
            edtWork.setText(requestDetail.manufacturingWork?.name ,false)
            edtWorkApprover.setText(requestDetail.approvers?.find { it.approveType == 1 }?.information?.firstOrNull()?.approver?.name ,false)
            edtStock.setText(requestDetail.stock?.name,false)
            if(requestDetail.type == 1) {
                tilUnit.isVisible = true
                tilBuyApprover.isVisible = true
                edtUnit.setText(requestDetail.orderUnit?.name,false)
                edtBuyApprover.setText(requestDetail.approvers?.find{it.approveType == 2}?.information?.firstOrNull()?.approver?.name ,false)
            } else {
                tilUnit.isVisible = false
                tilBuyApprover.isVisible = false
            }
            tilTime.isEnabled = isEdit
            tilWork.isEnabled = isEdit
            tilWorkApprover.isEnabled = isEdit
            tilStock.isEnabled = isEdit
            tilStatus.isVisible = true
            tilUnit.isEnabled = isEdit
            tilBuyApprover.isEnabled = isEdit
            tilDes.isEnabled = isEdit
            lnSave.isVisible = isEdit
            btnSave.text = "Lưu"
            tilProductType.isVisible = isEdit
            tilProduct.isVisible = isEdit
            tilBaseUnit.isVisible = isEdit
            tilQuantity.isVisible = isEdit
            btnAdd.isVisible = isEdit
            btnMenuMore.isVisible = isEdit
            listGood = (requestDetail.goods?.map { GoodDetailModel(quantity = it.quantity?.toLong() ?: 0L , _id = it.good._id
                , name = it.good.name, baseUnit = it.good.baseUnit , code = it.good.code) })?.toMutableList() ?: mutableListOf()
            adapter.isEdit = isEdit
            adapter.items = listGood
            when(requestDetail.type){
                1 -> {
                    when(requestDetail.status) {
                        1 -> {
                            edtStatus.setText("Chờ phê duyệt")
                            edtStatus.setTextColorz(R.color.clr_status_wait)
                        }
                        2 -> {
                            edtStatus.setText("Yêu cầu đã gửi đến bộ phận mua hàng")
                            edtStatus.setTextColorz(R.color.clr_wait_confirm_grab)
                        }
                        3 -> {
                            edtStatus.setText("đã phê duyệt mua hàng")
                            edtStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        4 -> {
                            edtStatus.setText("Đã tạo đơn mua hàng")
                            edtStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        5 -> {
                            edtStatus.setText("Chờ phê duyệt yêu cầu")
                            edtStatus.setTextColorz(R.color.clr_status_wait)
                        }
                        6 -> {
                            edtStatus.setText("Đã gửi yêu cầu nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        7 -> {
                            edtStatus.setText("Đã phê duyệt yêu cầu nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        8 -> {
                            edtStatus.setText("Đang tiến hành nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_inprogress)
                        }
                        9 -> {
                            edtStatus.setText("Đã hoàn thành nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_finish)
                        }
                        10 -> {
                            edtStatus.setText("Đã hủy yêu cầu mua hàng")
                            edtStatus.setTextColorz(R.color.clr_status_cancel)
                        }
                        11 -> {
                            edtStatus.setText("Đã hủy yêu cầu nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_cancel)
                        }
                    }
                }
                2 -> {
                    when (requestDetail.status) {
                        1 -> {
                            edtStatus.setText("Chờ phê duyệt")
                            edtStatus.setTextColorz(R.color.clr_status_wait)
                        }
                        2 -> {
                            edtStatus.setText("Yêu cầu đã gửi đến kho")
                            edtStatus.setTextColorz(R.color.clr_wait_confirm_grab)
                        }
                        3 -> {
                            edtStatus.setText("Đã phê duyệt yêu cầu nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        4 -> {
                            edtStatus.setText("Đang tiến hành nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_inprogress)
                        }
                        5 -> {
                            edtStatus.setText("Đã hoàn thành nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_finish)
                        }
                        6 -> {
                            edtStatus.setText("Đã hủy yêu cầu nhập kho")
                            edtStatus.setTextColorz(R.color.clr_status_cancel)
                        }
                    }
                }
                3 -> {
                    when (requestDetail.status) {
                        1 -> {
                            edtStatus.setText("Chờ phê duyệt")
                            edtStatus.setTextColorz(R.color.clr_status_wait)
                        }
                        2 -> {
                            edtStatus.setText("Yêu cầu đã gửi đến kho")
                            edtStatus.setTextColorz(R.color.clr_wait_confirm_grab)
                        }
                        3 -> {
                            edtStatus.setText("ĐĐã phê duyệt yêu cầu xuất kho")
                            edtStatus.setTextColorz(R.color.clr_status_approve)
                        }
                        4 -> {
                            edtStatus.setText("Đang tiến hành xuất kho")
                            edtStatus.setTextColorz(R.color.clr_status_inprogress)
                        }
                        5 -> {
                            edtStatus.setText("Đã hoàn thành xuất kho")
                            edtStatus.setTextColorz(R.color.clr_status_finish)
                        }
                        6 -> {
                            edtStatus.setText("Đã hủy yêu cầu xuất kho")
                            edtStatus.setTextColorz(R.color.clr_status_cancel)
                        }
                    }

                }
            }

        }
    }

    fun showDialog() {
        if (dialog != null || dialog?.showsDialog == true) {
            dialog?.dismiss()
            dialog = null
        }
        dialog = BottomDialogOptionRequest(requestDetail, config)
        dialog?.onConfirm = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.title_alert))
                .setMessage("Xác nhận phê duyệt phiếu?")
                .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    viewModel.updateStatusProductRequest(ParamUpdateRequest(1,config.getUser().id,null),requestId)
                    dialog.dismiss()
                }
                .show()
        }
        dialog?.onCancel = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.title_alert))
                .setMessage("Xác nhận hủy phiếu?")
                .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                    viewModel.updateStatusProductRequest(ParamUpdateRequest(null,null,if(requestDetail.type == 1) 10 else 6),requestId)
                    dialog.dismiss()
                }
                .show()
        }
        dialog?.show(childFragmentManager,"BottomDialogOptionRequest")

    }

}