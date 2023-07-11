package com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotDetailModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.ManufacturingWorkModel
import com.example.test.dxworkspace.data.entity.manufacturing_work.OrganizationUnit
import com.example.test.dxworkspace.data.entity.product_request.ParamApprover
import com.example.test.dxworkspace.data.entity.product_request.ParamCreateProductRequest
import com.example.test.dxworkspace.data.entity.product_request.ParamGood
import com.example.test.dxworkspace.data.entity.product_request.ParamInformation
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentImportProductNewBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.SubLot
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.adapter.InfoBillImportNewAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.ManufacturingRequestViewModel
import com.example.test.dxworkspace.presentation.utils.common.generateCode
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getddMMYYYY
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ImportProductFragmentNew : BaseFragment<FragmentImportProductNewBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_import_product_new
    }
    @Inject
    lateinit var viewModel: ManufacturingRequestViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var lotViewModel : ManufacturingLotViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

    @Inject
    lateinit var configRepository: ConfigRepository
    val calendar = Calendar.getInstance()

    var lot = ManufacturingLotDetailModel()
    var allUser = listOf<UserProfileResponse>()
    var allUnit = listOf<OrganizationUnit>()
    var allStock = listOf<StockModel>()
    var allWork = listOf<ManufacturingWorkModel>()
    var type = 2
    var requestNow = ParamCreateProductRequest()
    var listRequest = mutableListOf<ParamCreateProductRequest>()
    var requestAdapter = InfoBillImportNewAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lot = arguments?.getParcelable<ManufacturingLotDetailModel>("LOT") ?: lot
        viewModel = viewModel(viewModelFactory){
            observe(statusUpdate) {
                if (it == false) showToast(EventToast(text = "Tạo phiếu đề nghị  nhập kho thất bại"))
                else {
                    showToast(EventToast(isFail = false, text = "Tạo phiếu đề nghị nhập kho thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_LOT))
                    onBackPress()
                }
            }



        }
        observe(homeViewModel.listStock) {
            allStock = it ?: allStock
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
            if (lot._id.isNotEmpty()) allWork = allWork.filter {
                it.manufacturingMills?.map { it._id }
                    ?.contains(lot.manufacturingCommand?.manufacturingMill?._id) == true
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
        observeLoading(homeViewModel)
        observeLoading(viewModel)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getManufacturingWorksWithoutRole()
        homeViewModel.getStock()
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            rcvBill.adapter = requestAdapter
            edtProductCode.setText(lot.good?.code)
            edtProductName.setText(lot.good?.name)
            edtUnit.setText(lot.good?.baseUnit)
            edtQuantity.setText("0")
            requestAdapter.lot = lot
            edtMaxQuantity.setText(lot.originalQuantity.toString())
            requestNow.code = generateCode("GRR")
            requestNow.lotId = lot._id
            requestNow.status = 1
            requestNow.requestType = 1
            requestNow.type = type
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
            btnAddBill.setOnClickListener {
                if(requestNow.stock.isEmpty() || requestNow.approvers?.isEmpty() == true || requestNow.desiredTime.isEmpty() ){
                    showToast(EventToast(isFail = true, text = "Vui lòng nhập đủ thông tin"))
                    return@setOnClickListener
                }
                val t = edtQuantity.getTextz()
                if(t.isEmpty()) {
                    showToast(EventToast(isFail = true, text = "Số lượng không hợp lệ"))
                    return@setOnClickListener
                } else if(t.toInt() > edtMaxQuantity.getTextz().toInt()){
                    showToast(EventToast(isFail = true, text = "Số lượng không hợp lệ"))
                    return@setOnClickListener
                } else {
                    requestNow.goods = mutableListOf(ParamGood(lot.good!!._id,edtQuantity.getTextz(),
                        mutableListOf(SubLot(lot._id,edtQuantity.getTextz().toInt()))))
                    requestNow.description = edtDes.getTextz()
                    listRequest.add(requestNow)
                    requestNow = ParamCreateProductRequest(code = generateCode("GRR"))
                    requestNow.lotId = lot._id
                    requestNow.status = 1
                    requestNow.requestType = 1
                    requestNow.type = type
                    requestAdapter.items = listRequest
                    reset()
                }

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
                var total = 0
                listRequest.forEach {
                    total += (it.goods?.firstOrNull()?.quantity ?: "0").toInt()
                }
                edtMaxQuantity.setText(((lot.originalQuantity ?: 0) - total).toString())
            }
            btnSave.setOnClickListener {
                var total = 0
                listRequest.forEach {
                    total += (it.goods?.firstOrNull()?.quantity ?: "0").toInt()
                }
                if(total<lot.quantity ?: 0) {
                    showToast(EventToast(isFail = true, text = "Vui lòng nhập hết sản phẩm nhập kho"))
                    return@setOnClickListener
                } else {
                    lotViewModel.updateLot(lot._id,2)
                    viewModel.createManyProductRequest(listRequest)
                }
            }
        }
    }

    fun reset(){
        binding?.apply {
            edtCode.setText(requestNow.code)
            if(requestNow.desiredTime.isEmpty()) edtTime.setText("") else edtTime.setText(
                getddMMYYYY(requestNow.desiredTime))
            edtDes.setText(requestNow.description)
            if(requestNow.manufacturingWork.isEmpty()) edtWork.setText("",false) else edtWork.setText(allWork.first().toString() , false)
            if(requestNow.approvers?.isEmpty() == true) edtWorkApprover.setText("",false) else {
                val t = allWork.first().organizationalUnit?.managers?.first()?.users?.find { it.userId?.id == requestNow.approvers?.first()?.information?.first()?.approver }
                edtWorkApprover.setText(t?.userId?.name)
            }
            if(requestNow.stock.isEmpty()) edtStock.setText("",false) else edtStock.setText(allStock.find { it._id == requestNow.stock }?.name,false)
            edtQuantity.setText(requestNow.goods?.firstOrNull()?.quantity)
            var total = 0
            listRequest.forEach {
                    total += (it.goods?.firstOrNull()?.quantity ?: "0").toInt()
            }
            edtMaxQuantity.setText(((lot.originalQuantity ?: 0) - total).toString())
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