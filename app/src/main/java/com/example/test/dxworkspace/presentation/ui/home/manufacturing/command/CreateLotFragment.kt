package com.example.test.dxworkspace.presentation.ui.home.manufacturing.command

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.databinding.FragmentCreateLotBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.model.menu.RequestCreateLot
import com.example.test.dxworkspace.presentation.model.menu.RequestFinishCommand
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.utils.*
import com.example.test.dxworkspace.presentation.utils.common.generateCode
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.custom_toast.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CreateLotFragment() : BaseFragment<FragmentCreateLotBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_create_lot
    }


    val calendar = Calendar.getInstance()

    var lotRequestProduct = RequestCreateLot()
    var lotRequestWaste = RequestCreateLot()

    @Inject
    lateinit var viewModel: ManufacturingCommandViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var configRepository: ConfigRepository

    var command: ManufacturingCommandModel = ManufacturingCommandModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        command = arguments?.getParcelable<ManufacturingCommandModel>("COMMAND") ?: command
        viewModel = viewModel(viewModelFactory){
            observe(statusUpdate){
                if(it == false ) showToast(EventToast(text ="Update thất bại"))
                else {
                    showToast(EventToast(isFail = false, text ="Update thành công"))
                    EventBus.getDefault().post(EventUpdate(EventUpdate.UPDATE_COMMAND))
                    onBackPress()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         lotRequestProduct = RequestCreateLot(code = generateCode("LTP"), creator = configRepository.getUser().id,
        type = "product", productType = 1, manufacturingCommand = command._id, good = command.good._id!!, status = 1)
         lotRequestWaste = RequestCreateLot(code = generateCode("LTP"), creator = configRepository.getUser().id,
            type = "product", productType = 2, manufacturingCommand = command._id, good = command.good._id!!, status = 1)
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            edtCommandCode.setText(command.code)
            edtPlanCode.setText(command.good.name)
            edtOrderCode.setText(command.good.baseUnit)
            edtProductCode.setText(lotRequestProduct.code)
            edtWasteCode.setText(lotRequestWaste.code)
            edtQuantity.setText(command.quantity.toString())
            edtProductQuantity.doAfterTextChanged {
                lotRequestProduct.quantity = if(edtProductQuantity.getTextz() == "") "0" else edtProductQuantity.getTextz()
                lotRequestProduct.originalQuantity = lotRequestProduct.quantity
            }
            edtWasteQuantity.doAfterTextChanged {
                lotRequestWaste.quantity = if(edtWasteQuantity.getTextz() == "") "0" else edtWasteQuantity.getTextz()
                lotRequestWaste.originalQuantity = lotRequestWaste.quantity
            }
            edtProductDate.setOnClickListener {
                showMaterialDatePickerDialog(true)
            }
            edtWasteDate.setOnClickListener {
                showMaterialDatePickerDialog(false)
            }
            btnSave.setOnClickListener {
                if(lotRequestProduct.quantity=="0") {
                    showToast(EventToast(text = "Vui lòng chọn đủ thông tin"))
                    return@setOnClickListener
                } else {
                    val list = mutableListOf<RequestCreateLot>()
                    list.add(lotRequestProduct)
                    if(lotRequestWaste.quantity != "0") list.add(lotRequestWaste)
                    viewModel.finishCommand(RequestFinishCommand(lotRequestProduct.quantity,lotRequestWaste.quantity,getDateTimer(),4),command._id)
                    viewModel.createLot(list)
                }
            }

        }
    }
    private fun showMaterialDatePickerDialog(isProduct: Boolean) {
        val selectedDateInMillis = calendar.timeInMillis
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setSelection(selectedDateInMillis)
        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            calendar.timeInMillis = selectedDateInMillis

            if (isProduct) {
                binding?.edtProductDate?.setText(
                    SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)
                )
                lotRequestProduct.expirationDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(calendar.time)
            } else {
                binding?.edtWasteDate?.setText(
                    SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(calendar.time)
                )
                lotRequestWaste.expirationDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(calendar.time)
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
    }
    private fun showDateTimePickerDialog(isProduct: Boolean) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                if (isProduct) {
                    binding?.edtProductDate?.setText(
                        SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    lotRequestProduct.expirationDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendar.time)
                } else {
                    binding?.edtWasteDate?.setText(
                        SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(calendar.time)
                    )
                    lotRequestWaste.expirationDate = SimpleDateFormat(
                        "yyyy-MM-dd",
                        Locale.getDefault()
                    ).format(calendar.time)
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}