package com.example.test.dxworkspace.presentation.ui.timepicker

import android.os.Bundle
import android.view.View
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.databinding.FragmentDayBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.utils.common.Constants
import java.text.SimpleDateFormat
import java.util.*

class DayFragment : BaseFragment<FragmentDayBinding>() {

    lateinit var homeViewModel : HomeViewModel
    override fun getLayoutId(): Int {
        return R.layout.fragment_day
    }

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
            lnToday.setOnClickListener {
                if (cbToday.isChecked) return@setOnClickListener
                cbToday.isChecked = true
                cbYesterday.isChecked = false
            }
            lnYesterday.setOnClickListener {
                if (cbYesterday.isChecked) return@setOnClickListener
                cbToday.isChecked = false
                cbYesterday.isChecked = true
            }
        }
        when (homeViewModel.typeTimeReport) {
            Constants.DatePicker.QUICK_TODAY -> {
                binding!!.cbToday.isChecked = true
                binding!!.cbYesterday.isChecked = false
            }
            Constants.DatePicker.QUICK_YESTERDAY -> {
                binding!!.cbToday.isChecked = false
                binding!!.cbYesterday.isChecked = true
            }
        }
    }

    fun getRangeTimeSelected(){
//        if(binding!!.cbToday.isChecked){
//            // trả về 2 cặp Pair(start , to ) , cặp đầu có start và to bằng nhau và có giá trị là ngày hôm nay , biểu diễn dưới dạng "dd-MM-yyyy"
//            // cặp sau cũng có start và to băng nhau nhưng có giá trị bằng ngày hôm qua
//        } else {
//            // trả về 2 cặp Pair(start , to ) , cặp đầu có start và to bằng nhau và có giá trị là ngày hôm  , biểu diễn dưới dạng "dd-MM-yyyy"
//            // cặp sau cũng có start và to băng nhau nhưng có giá trị bằng ngày hôm kia
//        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()

        if (binding!!.cbToday.isChecked) {
            homeViewModel.typeTimeReport = Constants.DatePicker.QUICK_TODAY
            val today = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val yesterday = dateFormat.format(calendar.time)
            homeViewModel.fromDate = today
            homeViewModel.toDate = today
            homeViewModel.fromDateCompare = yesterday
            homeViewModel.toDateCompare= yesterday
            println("from ${homeViewModel.fromDate} to ${homeViewModel.toDate}")
            println("from compare ${homeViewModel.fromDateCompare} to compare ${homeViewModel.toDateCompare}")
        } else {
            homeViewModel.typeTimeReport = Constants.DatePicker.QUICK_YESTERDAY
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val today = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            val yesterday = dateFormat.format(calendar.time)
            homeViewModel.fromDate = today
            homeViewModel.toDate = today
            homeViewModel.fromDateCompare = yesterday
            homeViewModel.toDateCompare= yesterday
            println("from ${homeViewModel.fromDate} to ${homeViewModel.toDate}")
            println("from compare ${homeViewModel.fromDateCompare} to compare ${homeViewModel.toDateCompare}")
        }
        homeViewModel.isCompare = true
    }


}