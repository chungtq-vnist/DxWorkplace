package com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.StockModel
import com.example.test.dxworkspace.data.entity.manufacturing_lot.ManufacturingLotDetailModel
import com.example.test.dxworkspace.data.entity.user.UserProfileResponse
import com.example.test.dxworkspace.databinding.FragmentManufacturingLotDetailBinding
import com.example.test.dxworkspace.presentation.model.menu.RequestUpdateInfoLot
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.InfoQualityAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.adapter.ListEmployeeAdapter
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.common.postNormal
import com.example.test.dxworkspace.presentation.utils.common.setTextColorz
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.example.test.dxworkspace.presentation.utils.getddMMYYYY
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ManufacturingLotDetailFragment : BaseFragment<FragmentManufacturingLotDetailBinding>() {

    var lotId = ""
    var lot = ManufacturingLotDetailModel()
    var listUsers = listOf<UserProfileResponse>()
    var listStock = listOf<StockModel>()
    val adapterPerformer by lazy { ListEmployeeAdapter() }
    val adapterOperator by lazy { ListEmployeeAdapter() }
    val qualityAdapter by lazy { InfoQualityAdapter() }
    val calendar = Calendar.getInstance()


    override fun getLayoutId(): Int {
        return R.layout.fragment_manufacturing_lot_detail
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel: ManufacturingLotViewModel

    @Inject
    lateinit var homeViewModel : HomeViewModel

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
            EventUpdate.UPDATE_LOT -> {
                viewModel.getLotById(lotId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lotId = arguments?.getString("LOT_ID") ?: ""
        viewModel = viewModel(viewModelFactory){
            observe(lotDetail){
                lot = it ?: lot
                setupUI()
            }
            observe(updateStatus){
                if(it == false) showToast(EventToast(text ="Update thất bại"))
                else {
                    showToast(EventToast(isFail = false , text ="Update thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_LOT))
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
            }

        }
        viewModel.getLotById(lotId)
    }

    fun setupUI(){
        binding?.apply {
            edtLotCode.setText(lot.code)
            btnMenuMore.isVisible = false
            tilCommandCode.isEnabled = false
            tilLotCode.isEnabled = false
            tilImportCode.isEnabled = false
            tilLotType.isEnabled = false
            tilProductName.isEnabled = false
            tilTimeStart.isEnabled = false
            tilMillName.isEnabled = false
            tilStatus.isEnabled = false
            tilProductCode.isEnabled = false
            tilUnit.isEnabled = false
            edtCommandCode.setText(lot.manufacturingCommand?.code)
            edtImportCode.setText(lot.importStockRequest?.map{it.code}?.joinToString())
            edtMillName.setText(lot.manufacturingCommand?.manufacturingMill?.name)
            edtTimeStart.setText(getddMMYYYY(lot.createdAt))
            edtTimeEnd.setText(getddMMYYYY(lot.expirationDate ?: ""))
            edtLotType.setText(if(lot.productType == 1) "Phế phẩm" else "Thành phẩm")
            btnSave.isEnabled = false
            if(lot.status == 1){
                edtStatus.setText("Chưa lên đơn nhập kho")
                edtStatus.setTextColorz(R.color.clr_status_wait)
            } else if(lot.status == 2){
                edtStatus.setText("Đã lên đơn nhập kho")
                edtStatus.setTextColorz(R.color.clr_status_approve)
            } else {
                edtStatus.setText("Đã nhập kho")
                edtStatus.setTextColorz(R.color.clr_status_finish)
            }
            edtDes.setText(lot.description)
            edtProductCode.setText(lot.good?.code)
            edtProductName.setText(lot.good?.name)
            edtUnit.setText(lot.good?.baseUnit)
            edtQuantity.setText(lot.originalQuantity.toString())
            constraintOperator.setOnClickListener {
                rcvOperator.isVisible = !rcvOperator.isVisible
                ivExpandOperator.setImageResource(if(rcvOperator.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)

            }
            constraintPerformer.setOnClickListener {
                rcvPerformer.isVisible = !rcvPerformer.isVisible
                ivExpandPerformer.setImageResource(if(rcvPerformer.isVisible) R.drawable.ic_expand_up else R.drawable.ic_expand_down)
            }
            rcvOperator.adapter = adapterOperator
            rcvPerformer.adapter = adapterPerformer
            rcvQualityVerify.adapter = qualityAdapter
            adapterOperator.items = lot.manufacturingCommand?.accountables ?: listOf()
            adapterPerformer.items = lot.manufacturingCommand?.responsibles ?: listOf()
            qualityAdapter.items = lot.manufacturingCommand?.qualityControlStaffs ?: listOf()
            edtTimeEnd.setOnClickListener { showMaterialDatePickerDialog() }
            tilQuantity.isEnabled = lot.status == 1
            tilTimeEnd.isEnabled = lot.status == 1
            tilDes.isEnabled = lot.status == 1
            edtQuantity.doAfterTextChanged {
                val t = edtQuantity.getTextz()
                if(t == "") lot.originalQuantity = 0
                else lot.originalQuantity = t.toInt()
                btnSave.isSelected = true
                btnSave.isEnabled = true
            }
            btnImport.isVisible = lot.status == 1 && lot.importStockRequest.isNullOrEmpty()
            btnImport.setOnClickListener {
//                postNormal(EventNextHome(ImportProductFragment::class.java, bundleOf(Pair("LOT_ID",lotId))))
                postNormal(EventNextHome(ImportProductFragmentNew::class.java, bundleOf(Pair("LOT",lot))))
            }
            btnSave.setOnClickListener {
                viewModel.updateInfoLot(lotId, RequestUpdateInfoLot(edtDes.getTextz(),lot.expirationDate,lot.originalQuantity))
            }
        }
    }
    private fun showDateTimePickerDialog(isProduct: Boolean = true) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                if (isProduct) {
                    binding?.edtTimeEnd?.setText(
                        SimpleDateFormat(
                            "dd/MM/yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    lot.expirationDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendar.time)
                    binding?.btnSave?.apply {
                        isSelected = true
                        isEnabled = true
                    }
                } else {
//                    binding?.edtWasteDate?.setText(
//                        SimpleDateFormat(
//                            "dd-MM-yyyy",
//                            Locale.getDefault()
//                        ).format(calendar.time)
//                    )
//                    lotRequestWaste.expirationDate = SimpleDateFormat(
//                        "yyyy-MM-dd",
//                        Locale.getDefault()
//                    ).format(calendar.time)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showMaterialDatePickerDialog(isProduct: Boolean = true) {
        val selectedDateInMillis = calendar.timeInMillis

        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setSelection(selectedDateInMillis)

        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            calendar.timeInMillis = selectedDateInMillis
            if (isProduct) {
                binding?.edtTimeEnd?.setText(
                    SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)
                )
                lot.expirationDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(calendar.time)
                binding?.btnSave?.apply {
                    isSelected = true
                    isEnabled = true
                }
            } else {

            }
        }

        datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
    }

}