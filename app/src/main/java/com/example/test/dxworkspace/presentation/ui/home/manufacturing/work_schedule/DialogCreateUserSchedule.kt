package com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule

import android.R
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.test.dxworkspace.data.entity.manufacturing_command.ManufacturingCommandModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.ManufacturingMillModel
import com.example.test.dxworkspace.data.entity.manufacturing_mill.SubUserBasicModel
import com.example.test.dxworkspace.databinding.FragmentCreateMillScheduleBinding
import com.example.test.dxworkspace.databinding.FragmentCreateUserScheduleBinding
import com.example.test.dxworkspace.presentation.model.menu.RequestQualityControl
import com.example.test.dxworkspace.presentation.ui.home.workplace.time_picker.MonthYearPickerDialog
import com.example.test.dxworkspace.presentation.utils.common.getTextz
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import java.text.SimpleDateFormat
import java.util.*

class DialogCreateUserSchedule(
    val users: MutableList<SubUserBasicModel>,
    val months :String
) : DialogFragment() {
    lateinit var binding: FragmentCreateUserScheduleBinding
    var onAccept: ((SubUserBasicModel,String) -> Unit)? = null
    var user = SubUserBasicModel()
    var month = ""
    val calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateUserScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
            val t = ColorDrawable(Color.TRANSPARENT)
            val r = InsetDrawable(t,36)
            dialog.window?.setBackgroundDrawable(r)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkSave()
        month = months
        binding.apply {
            ivBack.setOnClickListener {
                dismiss()
            }
            btnSave.setOnClickListener {
                if(user._id.isEmpty() && user.name.isEmpty()) {
                    EventBus.getDefault().post(EventToast(text = "Vui lòng chọn đủ thông tin"))
                    return@setOnClickListener
                } else {
                    onAccept?.invoke(user,month)
                    dismiss()
                }
            }
            users.add(0,SubUserBasicModel(name = "Tất cả nhân viên"))
            edtManufacturingMill.setAdapter(ArrayAdapter(requireContext(),android.R.layout.simple_dropdown_item_1line,
                users))
            edtManufacturingMill.setOnItemClickListener { adapterView, view, i, l ->
                val m = (adapterView.getItemAtPosition(i) as SubUserBasicModel)
                user = m
            }
            edtShift.setText("3")
            edtMonth.setOnClickListener {
                showMonthYearTimePickerDialog()
            }
            val a = month.substringAfter("-").toInt()
            val b = month.substringBefore("-").toInt()
            calendar.set(Calendar.YEAR,b)
            calendar.set(Calendar.MONTH,a-1)
            binding.edtMonth.setText(SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(calendar.time))
        }
    }

    fun checkSave() {

    }
    private fun showMonthYearTimePickerDialog() {
        val time = month
        val dialog = MonthYearPickerDialog(
            requireContext(),
            time.substringAfter("-").toInt(),time.substringBefore("-").toInt()
        )
        dialog.onClick = { a, b ->
            calendar.set(Calendar.YEAR,b)
            calendar.set(Calendar.MONTH,a-1)
            month =  SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(calendar.time)
            binding.edtMonth.setText(SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(calendar.time))
        }
        dialog.show()
    }
}