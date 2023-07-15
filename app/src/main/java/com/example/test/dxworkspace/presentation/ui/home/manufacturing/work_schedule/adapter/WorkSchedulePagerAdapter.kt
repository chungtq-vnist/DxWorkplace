package com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule.MillWorkScheduleFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.work_schedule.UserWorkScheduleFragment

const val MILL_PAGE_INDEX = 0
const val USER_PAGE_INDEX = 1

class WorkSchedulePagerAdapter (fm : FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm,lc)  {
    private val millFg = MillWorkScheduleFragment()
    private val userFg = UserWorkScheduleFragment()


    override fun getItemCount(): Int {
        return tabFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position] as Fragment
    }

    private val listPage: Map<Int, Fragment> = mapOf(
        Pair(MILL_PAGE_INDEX, millFg),
        Pair(USER_PAGE_INDEX, userFg),

    )

    var tabFragmentsCreators: Map<Int, Fragment> = listPage

}