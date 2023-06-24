package com.example.test.dxworkspace.presentation.ui.timepicker

import android.os.Bundle
import android.view.View
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.FragmentWeekBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.utils.common.Constants
import java.text.SimpleDateFormat
import java.util.*

class WeekFragment : BaseFragment<FragmentWeekBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_week
    }
    lateinit var homeViewModel : HomeViewModel
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
            lnSevenDay.setOnClickListener {
                if (cbSevenDay.isChecked) return@setOnClickListener
                cbSevenDay.isChecked = true
                cbThisWeek.isChecked = false
                cbLastWeek.isChecked = false
            }
            lnThisWeek.setOnClickListener {
                if (cbThisWeek.isChecked) return@setOnClickListener
                cbSevenDay.isChecked = false
                cbThisWeek.isChecked = true
                cbLastWeek.isChecked = false
            }
            lnLastWeek.setOnClickListener {
                if (cbLastWeek.isChecked) return@setOnClickListener
                cbSevenDay.isChecked = false
                cbThisWeek.isChecked = false
                cbLastWeek.isChecked = true
            }
        }
        when (homeViewModel.typeTimeReport) {
            Constants.DatePicker.QUICK_THIS_WEEK -> {
                binding!!.cbThisWeek.isChecked = true
                binding!!.cbLastWeek.isChecked = false
                binding!!.cbSevenDay.isChecked = false
            }
            Constants.DatePicker.QUICK_PRE_WEEK -> {
                binding!!.cbThisWeek.isChecked = false
                binding!!.cbLastWeek.isChecked = true
                binding!!.cbSevenDay.isChecked = false
            }
            Constants.DatePicker.QUICK_7_DAY -> {
                binding!!.cbThisWeek.isChecked = false
                binding!!.cbLastWeek.isChecked = false
                binding!!.cbSevenDay.isChecked = true
            }
        }
    }

    fun getRangeTimeSelected() {
        if (binding!!.cbSevenDay.isChecked) {
            // trả về 2 cặp Pair(start , to ) , cặp đầu có to là ngày hiện tại , start là 6 ngày trước  , biểu diễn dưới dạng "dd-MM-yyyy"
            // cặp sau cũng có to là ngày trước của ngày start ở cặp đầu , start là 6 ngày trước
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val currentDate = Date()

            val calendar = Calendar.getInstance()
            calendar.time = currentDate

            // Cặp đầu tiên
            val startDate = calendar.clone() as Calendar
            startDate.add(Calendar.DAY_OF_MONTH, -6)
            val toDate = dateFormat.format(currentDate)
            val fromDate = dateFormat.format(startDate.time)

            startDate.add(Calendar.DAY_OF_MONTH, -1)
            val toDateCompare = dateFormat.format(startDate.time)
            startDate.add(Calendar.DAY_OF_MONTH, -6)
            val fromDateCompare = dateFormat.format(startDate.time)
            homeViewModel.fromDate = fromDate
            homeViewModel.toDate = toDate
            homeViewModel.fromDateCompare = fromDateCompare
            homeViewModel.toDateCompare= toDateCompare
            println("from ${homeViewModel.fromDate} to ${homeViewModel.toDate}")
            println("from compare ${homeViewModel.fromDateCompare} to compare ${homeViewModel.toDateCompare}")

        } else if(binding!!.cbThisWeek.isChecked){
            // trả về 2 cặp Pair(start , to ) , cặp đầu có to là ngày hiện tại , start là ngày đầu tiên của tuần hiện tại  , biểu diễn dưới dạng "dd-MM-yyyy"
            // cặp sau có to là ngày cuối cùng của tuần trước  , start là ngày đầu tiên của tuần trước
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val currentDate = Date()

            val calendar = Calendar.getInstance()
            calendar.time = currentDate

            // Cặp đầu tiên
            val startDate = calendar.clone() as Calendar
            startDate.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            val toDate = dateFormat.format(currentDate)
            val fromDate = dateFormat.format(startDate.time)

            // Cặp thứ hai
            val endDate = calendar.clone() as Calendar
            endDate.add(Calendar.WEEK_OF_YEAR, -1)
            endDate.set(Calendar.DAY_OF_WEEK, endDate.firstDayOfWeek + 6)
            val toDateCompare = dateFormat.format(endDate.time)
            endDate.set(Calendar.DAY_OF_WEEK, endDate.firstDayOfWeek)
            val fromDateCompare = dateFormat.format(endDate.time)
            homeViewModel.fromDate = fromDate
            homeViewModel.toDate = toDate
            homeViewModel.fromDateCompare = fromDateCompare
            homeViewModel.toDateCompare= toDateCompare
            println("from ${homeViewModel.fromDate} to ${homeViewModel.toDate}")
            println("from compare ${homeViewModel.fromDateCompare} to compare ${homeViewModel.toDateCompare}")
        } else {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val currentDate = Date()

            val calendar = Calendar.getInstance()
            calendar.time = currentDate

            // Cặp đầu tiên
            val startDate = calendar.clone() as Calendar
            startDate.add(Calendar.WEEK_OF_YEAR, -1)
            startDate.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            val fromDate = dateFormat.format(startDate.time)
            startDate.set(Calendar.DAY_OF_WEEK, startDate.firstDayOfWeek + 6)
            val toDate = dateFormat.format(startDate.time)

            // Cặp thứ hai
            val endDate = calendar.clone() as Calendar
            endDate.add(Calendar.WEEK_OF_YEAR, -2)
            endDate.set(Calendar.DAY_OF_WEEK, endDate.firstDayOfWeek + 6)
            val toDateCompare = dateFormat.format(endDate.time)
            endDate.set(Calendar.DAY_OF_WEEK, endDate.firstDayOfWeek)
            val fromDateCompare = dateFormat.format(endDate.time)
            homeViewModel.fromDate = fromDate
            homeViewModel.toDate = toDate
            homeViewModel.fromDateCompare = fromDateCompare
            homeViewModel.toDateCompare= toDateCompare
            println("from ${homeViewModel.fromDate} to ${homeViewModel.toDate}")
            println("from compare ${homeViewModel.fromDateCompare} to compare ${homeViewModel.toDateCompare}")
        }
        homeViewModel.isCompare = true
    }


}