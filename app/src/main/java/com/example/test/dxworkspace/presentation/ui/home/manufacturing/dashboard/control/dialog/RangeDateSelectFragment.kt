package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog

import com.example.test.dxworkspace.presentation.ui.home.workplace.time_picker.MonthYearPickerDialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.databinding.FragmentTimeMonthPickerBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.utils.convertToDate
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RangeDateSelectFragment : BaseFragment<FragmentTimeMonthPickerBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_time_month_picker

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel : HomeViewModel

    private var fromDate: String = ""
    private var toDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory){

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            fromDate = viewModel.fromDate
                toDate = viewModel.toDate
            tvStartTime.text = fromDate
            tvEndTime.text = toDate
            ivBack.setOnClickListener {
                onBackPress()
            }
            rlStart.setOnClickListener {
                showDatePickerDialog(true)

            }

            rlEnd.setOnClickListener {
                showDatePickerDialog(false)

            }

            ivDone.setOnClickListener {
                viewModel.fromDate = fromDate
                viewModel.toDate = toDate
                EventBus.getDefault().post(
                    EventUpdate(
                        EventUpdate.UPDATE_DASHBOARD_MANUFACTURING
                    )
                )
                onBackPress()
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

}