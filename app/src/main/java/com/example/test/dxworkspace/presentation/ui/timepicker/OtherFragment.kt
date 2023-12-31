package com.example.test.dxworkspace.presentation.ui.timepicker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.FragmentOtherBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.convertToDate
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class OtherFragment : BaseFragment<FragmentOtherBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_other

    lateinit var homeViewModel : HomeViewModel

    private var fromDate: String = ""
    private var toDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.parentFragment?.let {
            if(it is RangeTimeSelectFragment){
                homeViewModel = it.viewModel
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            fromDate = homeViewModel.fromDate
            toDate = homeViewModel.toDate
            tvStartTime.text = fromDate
            tvEndTime.text = toDate

            rlStart.setOnClickListener {
                showMaterialDatePickerDialog(true)

            }

            rlEnd.setOnClickListener {
                showMaterialDatePickerDialog(false)

            }

        }
    }

    private fun showDatePickerDialog(isFromDate: Boolean) {
        val calendar = Calendar.getInstance()
        calendar.time = convertToDate(if(isFromDate) fromDate else toDate)
        val year = calendar?.get(Calendar.YEAR) ?: Calendar.getInstance().get(Calendar.YEAR)
        val month = calendar?.get(Calendar.MONTH) ?: Calendar.getInstance().get(Calendar.MONTH)
        val dayOfMonth = calendar?.get(Calendar.DAY_OF_MONTH) ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, day ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.YEAR, selectedYear)
                selectedCalendar.set(Calendar.MONTH, selectedMonth)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, day)

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

                if (isFromDate) {
                    fromDate = dateFormat.format(selectedCalendar.time)
                    binding?.tvStartTime?.text = fromDate
                } else {
                    toDate = dateFormat.format(selectedCalendar.time)
                    binding?.tvEndTime?.text = toDate
                }
            }, year, month, dayOfMonth)

        datePickerDialog.show()
    }
    private fun showMaterialDatePickerDialog(isFromDate: Boolean) {
        val calendar = Calendar.getInstance()
        calendar.time = convertToDate(if (isFromDate) fromDate else toDate)
        val selectedDateInMillis = calendar.timeInMillis

        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setSelection(selectedDateInMillis)

        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.timeInMillis = selectedDateInMillis

            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            if (isFromDate) {
                fromDate = dateFormat.format(selectedCalendar.time)
                binding?.tvStartTime?.text = fromDate
            } else {
                toDate = dateFormat.format(selectedCalendar.time)
                binding?.tvEndTime?.text = toDate
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
    }

    fun getRangeTimeSelected(){
        homeViewModel.typeTimeReport = Constants.DatePicker.OTHER
        homeViewModel.fromDate = fromDate
        homeViewModel.toDate = toDate
        homeViewModel.isCompare = true
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val calendarA = Calendar.getInstance()
        calendarA.time = format.parse(fromDate)
        val calendarB = Calendar.getInstance()
        calendarB.time = format.parse(toDate)
        val range = calendarB.timeInMillis - calendarA.timeInMillis
        calendarA.add(Calendar.DAY_OF_MONTH, -1)
        val toDateCompare = format.format(calendarA.time)
        calendarB.timeInMillis = calendarA.timeInMillis - range
        val fromDateCompare = format.format(calendarB.time)
        homeViewModel.fromDateCompare = fromDateCompare
        homeViewModel.toDateCompare = toDateCompare
    }

}