package com.example.test.dxworkspace.presentation.ui.home.workplace.time_picker

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.FragmentTimeMonthPickerBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import java.util.*

class RangeMonthFragment : BaseFragment<FragmentTimeMonthPickerBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_time_month_picker
    var fromMonth = ""
    var toMonth = ""
    private var fromDate: Calendar? = null
    private var toDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromMonth = arguments?.getString("FROM_MONTH", "1-2023").toString()
        toMonth = arguments?.getString("TO_MONTH", "1-2023").toString()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            tvStartTime.text = fromMonth
            tvEndTime.text = toMonth
            ivBack.setOnClickListener {
                onBackPress()
            }
            rlStart.setOnClickListener {
                showMonthYearTimePickerDialog(true)

            }

            rlEnd.setOnClickListener {
                showMonthYearTimePickerDialog(false)

            }

            ivDone.setOnClickListener {
                EventBus.getDefault().post(
                    EventUpdate(
                        EventUpdate.UPDATE_TIME_FILTER_HOME,
                        Pair(fromMonth, toMonth)
                    )
                )
                onBackPress()
            }
        }
    }

    private fun showDatePickerDialog(isFromDate: Boolean) {
        val calendar = if (isFromDate) fromDate else toDate

        val year = calendar?.get(Calendar.YEAR) ?: Calendar.getInstance().get(Calendar.YEAR)
        val month = calendar?.get(Calendar.MONTH) ?: Calendar.getInstance().get(Calendar.MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, _ ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.YEAR, selectedYear)
                selectedCalendar.set(Calendar.MONTH, selectedMonth)

                if (isFromDate) {
                    fromDate = selectedCalendar
                    binding?.tvStartTime?.text = formatDate(selectedCalendar)
                } else {
                    toDate = selectedCalendar
                    binding?.tvEndTime?.text = formatDate(selectedCalendar)
                }
            }, year, month, 1)
        datePickerDialog.datePicker.findViewById<View>(
            resources.getIdentifier(
                "day",
                "id",
                "android"
            )
        ).visibility = View.GONE
//

        datePickerDialog.show()
    }

    private fun showMonthYearTimePickerDialog(isFromDate: Boolean) {
        val time = if (isFromDate) fromMonth else toMonth
        val dialog = MonthYearPickerDialog(
            requireContext(), time.substringBefore("-").toInt(),
            time.substringAfter("-").toInt()
        )
        dialog.onClick = { a, b ->
            if (isFromDate) {
                fromMonth = "$a-$b"
                binding?.tvStartTime?.text = fromMonth

            } else {
                toMonth = "$a-$b"
                binding?.tvEndTime?.text = toMonth
            }
        }
        dialog.show()
    }

    private fun formatDate(calendar: Calendar): String {
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        return "$month $year"
    }

    private fun initDate() {

    }
}