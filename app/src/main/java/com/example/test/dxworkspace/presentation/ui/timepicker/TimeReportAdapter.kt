package com.example.test.dxworkspace.presentation.ui.timepicker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test.dxworkspace.presentation.ui.BaseFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog.RangeDateSelectFragment

class TimeReportAdapter(fm: FragmentManager, lc: Lifecycle) : FragmentStateAdapter(fm, lc) {

    private val dayFragment = DayFragment()
    private val monthFragment = MonthFragment()
    private val weekFragment = WeekFragment()
    private val otherFragment = OtherFragment()

    override fun getItemCount(): Int {
       return tabFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position] as Fragment
    }

    private val tabFragmentsCreators: Map<Int, Fragment> = mapOf(
        Pair(0, dayFragment),
        Pair(1, weekFragment),
        Pair(2, monthFragment),
        Pair(3, otherFragment)
    )

    fun getCurrentFragment (position: Int) : Fragment{
       return if(position == 0) return dayFragment else if(position == 1) weekFragment else if(position == 2) monthFragment else otherFragment
    }
}