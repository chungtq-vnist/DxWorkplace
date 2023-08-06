package com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.quality.DashboardQualityManufacturingFragment

const val PLAN_PAGE_INDEX = 0
const val COMMAND_PAGE_INDEX = 1
const val REQUEST_PAGE_INDEX = 2
const val QUALITY_PAGE_INDEX = 3

class DashboardControlPagerAdapter(fm : FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm,lc) {


    private val planFragment = DashboardManufacturingPlanFragment()
    private val commandFragment = DashboardManufacturingCommandFragment()
    private val requestFragment = DashboardManufacturingRequestFragment()
    private val qualityFragment = DashboardQualityManufacturingFragment()

    override fun getItemCount(): Int {
        return tabFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position] as Fragment
    }

    private val listPage: Map<Int, Fragment> = mapOf(
        Pair(PLAN_PAGE_INDEX, planFragment),
        Pair(COMMAND_PAGE_INDEX, commandFragment),
        Pair(REQUEST_PAGE_INDEX, requestFragment),
        Pair(QUALITY_PAGE_INDEX, qualityFragment),
//        Pair(PAY_PAGE_INDEX, payFragment),
//        Pair(QR_ORDER_INDEX,qrOrderFragment)
    )
//    private val listNoTable: Map<Int, Fragment> = mapOf(
//            Pair(BILL_PAGE_INDEX, billFragment),
//            Pair(AREA_PAGE_INDEX, payFragment)
//    )

    //    var tabFragmentsCreators: Map<Int, Fragment> = if (hasTable) listHasTable else listNoTable
    var tabFragmentsCreators: Map<Int, Fragment> = listPage

}