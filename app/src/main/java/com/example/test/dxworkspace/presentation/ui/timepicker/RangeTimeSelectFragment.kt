package com.example.test.dxworkspace.presentation.ui.timepicker

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.databinding.FragmentTimeReportPickerBinding
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class RangeTimeSelectFragment : BaseFragment<FragmentTimeReportPickerBinding>()  {
    override fun getLayoutId(): Int {
        return R.layout.fragment_time_report_picker
    }

    val adapter by lazy { TimeReportAdapter(childFragmentManager , lifecycle) }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory){

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            ivBack.setOnClickListener { onBackPress() }
            viewPager.adapter = adapter
            viewPager.isUserInputEnabled = false
            tabs.tabRippleColor = null
            tabs.addTab(tabs.newTab().setText("Ngày"), 0)
            tabs.addTab(tabs.newTab().setText("Tuần"), 1)
            tabs.addTab(tabs.newTab().setText("Tháng"), 2)
            tabs.addTab(tabs.newTab().setText("Khác"), 3)
            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPager.setCurrentItem(tab?.position ?: 0)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
//                    TODO("Not yet implemented")
                }

            })
            ivDone.setOnClickListener {
                val position = binding!!.viewPager.currentItem
                when (val currFragment = adapter.getCurrentFragment(position)) {
                    is DayFragment -> {
                        currFragment.getRangeTimeSelected()
                    }
                    is WeekFragment -> {
                         currFragment.getRangeTimeSelected()
                    }
                    is MonthFragment -> {
                        currFragment.getRangeTimeSelected()
                    }
////                    is YearFragment -> {
////                        return currFragment.getTimeRangeSelect()
////                    }
                    is OtherFragment -> {
                        currFragment.getRangeTimeSelected()
                    }
                }
                EventBus.getDefault().post(
                    EventUpdate(
                        EventUpdate.UPDATE_DASHBOARD_MANUFACTURING
                    )
                )
                onBackPress()
            }
        }
//        initCurrentPager()
    }

    fun initCurrentPager(){
        when(viewModel.typeTimeReport){
            Constants.DatePicker.QUICK_TODAY, Constants.DatePicker.QUICK_YESTERDAY -> {
                binding!!.viewPager.setCurrentItem(0, true)
            }
            Constants.DatePicker.QUICK_THIS_WEEK, Constants.DatePicker.QUICK_PRE_WEEK, Constants.DatePicker.QUICK_7_DAY -> {
                binding!!.viewPager.setCurrentItem(1, true)
            }
            Constants.DatePicker.QUICK_THIS_MONTH, Constants.DatePicker.QUICK_PRE_MONTH, Constants.DatePicker.QUICK_30_DAY -> {
                binding!!.viewPager.setCurrentItem(2, true)
            }
//            Constants.DatePicker.QUICK_THIS_YEAR, Constants.DatePicker.QUICK_PRE_YEAR, Constants.DatePicker.QUICK_365_DAY -> {
//                binding!!.viewPager.setCurrentItem(3, true)
//            }
            Constants.DatePicker.OTHER -> {
                binding!!.viewPager.setCurrentItem(3, true)
            }
        }
    }



}