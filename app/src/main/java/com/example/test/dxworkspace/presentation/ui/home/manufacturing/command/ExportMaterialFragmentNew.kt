package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.di.viewmodel.ViewModelFactory
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.good.InventoryGoodWrap
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.SubMaterialModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.product_request.ParamApprover
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.data.entity.product_request.ParamGood
import com.example.test.dxworkspace.data.entity.product_request.ParamInformation
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.DialogExportMaterialBinding
import com.example.test.dxworkspace.databinding.FragmentExportMaterialNewBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.domain.usecase.manufacturing_manage.GetAllUsersUseCase
import com.example.test.dxworkspace.presentation.model.menu.*
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.*
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.adapter.InfoBillImportNewAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.ManufacturingRequestViewModel
import com.example.test.dxworkspace.presentation.ui.home.workplace.ChooseUserFragment
import com.example.test.dxworkspace.presentation.utils.common.generateCode
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getddMMYYYY
import com.google.android.material.datepicker.MaterialDatePicker
import javax.inject.Inject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.custom_toast.*
import java.text.SimpleDateFormat
import java.util.*


class ExportMaterialFragmentNew(

) : BaseFragment<FragmentExportMaterialNewBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_export_material_new
    }

    @Inject
    lateinit var viewModel: ManufacturingRequestViewModel

    @Inject
    lateinit var commandViewModel: ManufacturingCommandViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var configRepository: ConfigRepository

    @Inject
    lateinit var homeViewModel: HomeViewModel

    val calendar = Calendar.getInstance()
    var requestNow = ParamCreateProductRequest()
    var listRequest = mutableListOf<ParamCreateProductRequest>()
    var requestAdapter = InfoRequestExportAdapter()

    var quantity: Int = 1
    var command: ManufacturingCommandModel = ManufacturingCommandModel()
    var listInventory = listOf<InventoryGoodWrap>()

    var infoMateritalAdapter = InfoMaterialExportNewAdapter()
    val infoAdapter = InfoMaterialAdapter()

    var request = mutableListOf<BillExportMaterialRequest>()
    var materialNow = SubMaterialModel()

    var lUser = listOf<UserProfileResponse>()
    var allStock = listOf<StockModel>()
    var allWork = listOf<ManufacturingWorkModel>()

    var type = 3


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
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quantity = arguments?.getInt("QUANTITY") ?: 1
        command = arguments?.getParcelable<ManufacturingCommandModel>("COMMAND") ?: command
        val t = arguments?.getString("INVENTORY") ?: ""
        listInventory = gson.fromJson(t, object : TypeToken<List<InventoryGoodWrap?>?>() {}.type)
        viewModel = viewModel(viewModelFactory) {
            observe(statusUpdate) {
                if (it == false) showToast(EventToast(text = "Tạo phiếu xuất kho thất bại"))
                else {
                    showToast(EventToast(isFail = false, text = "Tạo phiếu xuất thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_COMMAND))
                    onBackPress()
                }
            }
        }
        commandViewModel = viewModel(viewModelFactory) {

        }

        observeLoading(homeViewModel)
        observeLoading(viewModel)
        observe(homeViewModel.listStock) {
            allStock = it ?: listOf()
            requestAdapter.listStock = allStock
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
                    requestNow.stock = t._id
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
                    requestNow.manufacturingWork = t._id
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.v("TAG",viewModel)
        homeViewModel.getStock()
        homeViewModel.getManufacturingWorksWithoutRole()
        requestNow.code = generateCode("GIR")
        requestNow.status = 1
        requestNow.requestType = 1
        requestNow.type = type
        requestNow.commandId = command._id
        binding?.apply {
            // setup thong tin nvl can xuat
            rcvBill.adapter = requestAdapter
            rcvInfoMaterial.adapter = infoAdapter
            infoAdapter.listInventory = listInventory
            infoAdapter.quantity = quantity
            infoAdapter.items = command.good.materials ?: listOf()
            btnAddBill.isVisible = false
            rcvMaterialExport.adapter = infoMateritalAdapter
            infoMateritalAdapter.onDelete = { t ->
                requestNow.goods?.removeAt(t)
                if (materialNow.good?._id?.isNotEmpty() == true) {
                    val totalNeed = quantity * (materialNow.quantity ?: 0)
                    var exported = 0
                    listRequest.forEach { k ->
                        val t =
                            k.goods?.find { it.good == materialNow.good?._id }?.quantity?.toInt()
                        exported += t ?: 0
                    }
                    exported += requestNow.goods?.find { it.good == materialNow.good?._id }?.quantity?.toInt()
                        ?: 0

                    edtNeedToExport.setText((totalNeed - exported).toString())
                }
                infoMateritalAdapter.notifyDataSetChanged()
                binding?.btnAddBill?.isVisible = requestNow.goods?.isEmpty() == false
            }
            requestAdapter.onEdit = { p ->
                requestNow = listRequest[p]
                listRequest.removeAt(p)
                requestAdapter.notifyDataSetChanged()
                reset()
            }
            requestAdapter.onDelete = { p ->
                listRequest.removeAt(p)
                requestAdapter.notifyDataSetChanged()
                if (materialNow.good?._id?.isNotEmpty() == true) {
                    val totalNeed = quantity * (materialNow.quantity ?: 0)
                    var exported = 0
                    listRequest.forEach { k ->
                        val t =
                            k.goods?.find { it.good == materialNow.good?._id }?.quantity?.toInt()
                        exported += t ?: 0
                    }
                    exported += requestNow.goods?.find { it.good == materialNow.good?._id }?.quantity?.toInt()
                        ?: 0

                    edtNeedToExport.setText((totalNeed - exported).toString())
                }

            }
            infoMateritalAdapter.listMaterial = command.good.materials ?: listOf()
            edtVariantName.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    command.good.materials!!
                )
            )
            edtVariantName.setOnItemClickListener { adapterView, view, i, l ->
                val m = (adapterView.getItemAtPosition(i) as SubMaterialModel)
                materialNow = m
                val totalNeed = quantity * (m.quantity ?: 0)
                var exported = 0
                listRequest.forEach { k ->
                    val t = k.goods?.find { it.good == materialNow.good?._id }?.quantity?.toInt()
                    exported += t ?: 0
                }
                exported += requestNow.goods?.find { it.good == materialNow.good?._id }?.quantity?.toInt()
                    ?: 0

                edtNeedToExport.setText((totalNeed - exported).toString())
            }


            edtCode.setText(requestNow.code)
            edtTime.setOnClickListener {
                showMaterialDatePickerDialog()
            }
            edtStock.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as StockModel)
                requestNow.stock = t._id
            }
            edtWorkApprover.setOnItemClickListener { adapterView, view, i, l ->
                val t = (adapterView.getItemAtPosition(i) as UserProfileResponse)
                requestNow.approvers?.removeIf { it.approveType == 1 }
                requestNow.approvers?.add(
                    ParamInformation(
                        mutableListOf(ParamApprover(null, t.id)),
                        1
                    )
                )
            }

            btnSave.setOnClickListener {
                if (listRequest.isEmpty()) {
                    showToast(EventToast(text = "Vui lòng chọn đủ thông tin!"))
                    return@setOnClickListener
                } else {
                    var out = false
                    command.good.materials?.forEach { a ->
                        val total = (a.quantity ?: 0) * command.quantity
                        var total2 = 0
                        listRequest.forEach { b ->
                            total2 += b.goods?.find { it.good == a.good!!._id }?.quantity?.toInt()
                                ?: 0
                        }
                        if (total != total2) {
                            out = true
                        }
                    }
                    if (out) {
                        showToast(EventToast(text = "Vui lòng chọn đủ thông tin!"))
                        return@setOnClickListener
                    }



                    commandViewModel.updateCommand(
                        RequestApproveCommand(
                            configRepository.getUser().id,
                            2
                        ), command._id
                    )
                    viewModel.createManyProductRequest(listRequest)
//                    viewModel.createExportBills(request)
                }
            }

        }


//        billNow = BillExportMaterialRequest(code = generateCode("BILL"))
        binding?.apply {
//            edtCode.setText(billNow.code)
//            rcvInfoMaterial.adapter = infoAdapter
//            infoAdapter.listInventory = listInventory
//            infoAdapter.quantity = quantity
//            infoAdapter.items = command.good.materials ?: listOf()
//            btnAddBill.visibility = View.INVISIBLE
//            edtVariantName.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,command.good.materials!!))
//            edtVariantName.setOnItemClickListener { adapterView, view, i, l ->
//                val m = (adapterView.getItemAtPosition(i) as SubMaterialModel)
//                materialNow = m
//                edtNeedToExport.setText((quantity*(m.quantity!!)).toString())
//            }


//            btnAddProduct.setOnClickListener {
//                if(materialNow.quantity ?: 0 > 0 && materialNow.good?._id?.isNotEmpty() == true) {
//                    billNow.goods.add(
//                        Good(
//                            materialNow.good!!._id,
//                            edtExportQuantity.getTextz().toInt(),
//                            materialNow.good!!.code
//                        )
//                    )
//                    materialNow = SubMaterialModel()
//                    infoMateritalAdapter.items = billNow.goods
//                    btnAddBill.visibility = View.VISIBLE
//                } else showToast(EventToast(text = "Vui lòng chọn đủ thông tin!"))
//            }

            btnAddProduct.setOnClickListener {
                if (materialNow.good?._id?.isEmpty() == true || edtExportQuantity.getTextz()
                        .isEmpty()
                    || edtExportQuantity.getTextz().toInt() > edtNeedToExport.getTextz().toInt()
                ) {
                    showToast(EventToast(text = "Thông tin không hợp lê!"))
                    return@setOnClickListener
                } else {
                    val t = requestNow.goods?.find { it.good == materialNow.good?._id }
                    if (t == null) {
                        requestNow.goods?.add(
                            ParamGood(
                                materialNow.good!!._id,
                                edtExportQuantity.getTextz()
                            )
                        )
                    } else {
                        t.quantity =
                            (t.quantity.toInt() + edtExportQuantity.getTextz().toInt()).toString()
                    }
                    btnAddBill.isVisible = true
                    edtVariantName.setText("", false)
                    infoMateritalAdapter.items = requestNow.goods ?: mutableListOf()
                    edtNeedToExport.setText("")
                    edtExportQuantity.setText("0")
                    materialNow = SubMaterialModel()
                }
            }
            btnAddBill.setOnClickListener {
                if (requestNow.stock.isEmpty() || requestNow.approvers?.isEmpty() == true || requestNow.desiredTime.isEmpty()
                    || requestNow.goods?.isEmpty() == true
                ) {
                    showToast(EventToast(isFail = true, text = "Vui lòng nhập đủ thông tin"))
                    return@setOnClickListener
                }

                requestNow.description = edtDes.getTextz()
                listRequest.add(requestNow)
                requestAdapter.items = listRequest
                requestNow = ParamCreateProductRequest()
                requestNow.code = generateCode("GIR")
                requestNow.status = 1
                requestNow.requestType = 1
                requestNow.type = type
                requestNow.commandId = command._id

                reset()

            }

        }
    }

    // goi khi luu va khi chinh sua bill
    fun reset() {
        binding?.apply {
            edtCode.setText(requestNow.code)
            if (requestNow.desiredTime.isEmpty()) edtTime.setText("") else edtTime.setText(
                getddMMYYYY(requestNow.desiredTime)
            )
            edtDes.setText(requestNow.description)
            if (requestNow.manufacturingWork.isEmpty()) edtWork.setText(
                "",
                false
            ) else edtWork.setText(allWork.first { it._id == requestNow.manufacturingWork }
                .toString(), false)
            if (requestNow.approvers?.isEmpty() == true) edtWorkApprover.setText("", false) else {
                val t =
                    allWork.first().organizationalUnit?.managers?.first()?.users?.find { it.userId?.id == requestNow.approvers?.first()?.information?.first()?.approver }
                edtWorkApprover.setText(t?.userId?.name)
            }
            if (requestNow.stock.isEmpty()) edtStock.setText("", false) else edtStock.setText(
                allStock.find { it._id == requestNow.stock }?.name
            )
            edtVariantName.setText("", false)
            infoMateritalAdapter.items = requestNow.goods ?: mutableListOf()
            edtNeedToExport.setText("")
            edtExportQuantity.setText("0")
            materialNow = SubMaterialModel()


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
                requestNow.desiredTime =
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
    private fun showMaterialDatePickerDialog() {
        val selectedDateInMillis = calendar.timeInMillis

        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setSelection(selectedDateInMillis)

        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            calendar.timeInMillis = selectedDateInMillis

            binding?.edtTime?.setText(
                SimpleDateFormat(
                    "dd-MM-yyyy",
                    Locale.getDefault()
                ).format(calendar.time)
            )
            requestNow.desiredTime =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(
                    calendar.time
                )
        }

        datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
    }
}