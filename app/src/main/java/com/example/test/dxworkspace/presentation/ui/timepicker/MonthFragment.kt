package com.example.test.dxworkspace.presentation.ui.timepicker

import android.os.Bundle
import android.view.View
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.FragmentMonthBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.utils.common.Constants
import java.text.SimpleDateFormat
import java.util.*

class MonthFragment : BaseFragment<FragmentMonthBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_month
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
            lnThirtyDay.setOnClickListener {
                if (cbThirtyDay.isChecked) return@setOnClickListener
                cbThirtyDay.isChecked = true
                cbThisMonth.isChecked = false
                cbLastMonth.isChecked = false
            }
            lnThisMonth.setOnClickListener {
                if (cbThisMonth.isChecked) return@setOnClickListener
                cbThirtyDay.isChecked = false
                cbThisMonth.isChecked = true
                cbLastMonth.isChecked = false
            }
            lnLastMonth.setOnClickListener {
                if (cbLastMonth.isChecked) return@setOnClickListener
                cbThirtyDay.isChecked = false
                cbThisMonth.isChecked = false
                cbLastMonth.isChecked = true
            }
        }
        when (homeViewModel.typeTimeReport) {
            Constants.DatePicker.QUICK_THIS_MONTH -> {
                binding!!.cbThisMonth.isChecked = true
                binding!!.cbLastMonth.isChecked = false
                binding!!.cbThirtyDay.isChecked = false
            }
            Constants.DatePicker.QUICK_PRE_MONTH -> {
                binding!!.cbThisMonth.isChecked = false
                binding!!.cbLastMonth.isChecked = true
                binding!!.cbThirtyDay.isChecked = false
            }
            Constants.DatePicker.QUICK_30_DAY -> {
                binding!!.cbThisMonth.isChecked = false
                binding!!.cbLastMonth.isChecked = false
                binding!!.cbThirtyDay.isChecked = true
            }
        }
    }

    fun getRangeTimeSelected(){
        if(binding!!.cbThirtyDay.isChecked){
            // trả về 2 cặp Pair(start , to ) , cặp đầu có to là ngày hiện tại , start là 29 ngày trước  , biểu diễn dưới dạng "dd-MM-yyyy"
            // cặp sau cũng có to là ngày trước của ngày start ở cặp đầu , start là 29 ngày trước
            homeViewModel.typeTimeReport = Constants.DatePicker.QUICK_30_DAY
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val currentDate = Date()

            val calendar = Calendar.getInstance()
            calendar.time = currentDate


            // Cặp đầu tiên
            val startDate = calendar.clone() as Calendar
            startDate.add(Calendar.DAY_OF_YEAR, -29) // Trừ đi 29 ngày
            val toDate = dateFormat.format(currentDate)
            val fromDate = dateFormat.format(startDate.time)

            val fromPreviousDate = calendar.clone() as Calendar
            fromPreviousDate.add(Calendar.DAY_OF_YEAR, -30) // Trừ đi 30 ngày (ngày start của cặp đầu)
            val toDateCompare = dateFormat.format(fromPreviousDate.time)
            fromPreviousDate.add(Calendar.DAY_OF_YEAR, -29)
            val fromDateCompare = dateFormat.format(fromPreviousDate.time)
            homeViewModel.fromDate = fromDate
            homeViewModel.toDate = toDate
            homeViewModel.fromDateCompare = fromDateCompare
            homeViewModel.toDateCompare= toDateCompare
            println("from ${homeViewModel.fromDate} to ${homeViewModel.toDate}")
            println("from compare ${homeViewModel.fromDateCompare} to compare ${homeViewModel.toDateCompare}")

        } else if(binding!!.cbThisMonth.isChecked){
            // trả về 2 cặp Pair(start , to ) , cặp đầu có to là ngày hiện tại , start là ngày đầu tiên của tháng hiện tại  , biểu diễn dưới dạng "dd-MM-yyyy"
            // cặp sau  có start  là ngày đầu tiên của tháng trước , end là ngày cuối của tháng trước
            homeViewModel.typeTimeReport = Constants.DatePicker.QUICK_THIS_MONTH
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate

            val startDate = calendar.clone() as Calendar
            startDate.set(Calendar.DAY_OF_MONTH, 1) // Đặt ngày là ngày đầu tiên của tháng hiện tại
            val toDate = dateFormat.format(currentDate)
            val fromDate = dateFormat.format(startDate.time)

            // Cặp thứ hai
            val fromPreviousDate = calendar.clone() as Calendar
            fromPreviousDate.add(Calendar.MONTH, -1) // Trừ đi 1 tháng (tháng trước)
            fromPreviousDate.set(Calendar.DAY_OF_MONTH, 1) // Đặt ngày là ngày đầu tiên của tháng trước
            val fromDateCompare = dateFormat.format(fromPreviousDate.time)
            val toPreviousDate = fromPreviousDate.getActualMaximum(Calendar.DAY_OF_MONTH) // Lấy ngày cuối cùng của tháng trước
            fromPreviousDate.set(Calendar.DAY_OF_MONTH, toPreviousDate) // Đặt ngày là ngày cuối cùng của tháng trước
            val toDateCompare = dateFormat.format(fromPreviousDate.time)

            homeViewModel.fromDate = fromDate
            homeViewModel.toDate = toDate
            homeViewModel.fromDateCompare = fromDateCompare
            homeViewModel.toDateCompare= toDateCompare
            println("from ${homeViewModel.fromDate} to ${homeViewModel.toDate}")
            println("from compare ${homeViewModel.fromDateCompare} to compare ${homeViewModel.toDateCompare}")

        } else {
            homeViewModel.typeTimeReport = Constants.DatePicker.QUICK_PRE_MONTH
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate

            val startDate = calendar.clone() as Calendar
            startDate.add(Calendar.MONTH, -1)
            startDate.set(Calendar.DAY_OF_MONTH, 1) // Đặt ngày là ngày đầu tiên của tháng hiện tại
            val fromDate = dateFormat.format(startDate.time)
            val endateMonth = startDate.getActualMaximum(Calendar.DAY_OF_MONTH)
            startDate.set(Calendar.DAY_OF_MONTH, endateMonth)
            val toDate = dateFormat.format(startDate.time)

            // Cặp thứ hai
            val fromPreviousDate = calendar.clone() as Calendar
            fromPreviousDate.add(Calendar.MONTH, -2) // Trừ đi 1 tháng (tháng trước)
            fromPreviousDate.set(Calendar.DAY_OF_MONTH, 1) // Đặt ngày là ngày đầu tiên của tháng trước
            val fromDateCompare = dateFormat.format(fromPreviousDate.time)
            val toPreviousDate = fromPreviousDate.getActualMaximum(Calendar.DAY_OF_MONTH) // Lấy ngày cuối cùng của tháng trước
            fromPreviousDate.set(Calendar.DAY_OF_MONTH, toPreviousDate) // Đặt ngày là ngày cuối cùng của tháng trước
            val toDateCompare = dateFormat.format(fromPreviousDate.time)

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