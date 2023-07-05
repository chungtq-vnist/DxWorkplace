package com.example.test.dxworkspace.presentation.ui.home.report.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test.dxworkspace.presentation.ui.home.report.warehouse.ReportMaterialWarehouseFragment
import com.example.test.dxworkspace.presentation.ui.home.report.warehouse.ReportProductWarehouseFragment

const val OVERVIEW_PAGE_INDEX = 1
const val PRODUCT_PAGE_INDEX = 0
const val MATERIAL_PAGE_INDEX = 1
const val EQUIPMENT_TICKET_INDEX = 3
const val WASTE_ORDER_INDEX = 4
class WarehouseReportTabAdapter(fm : FragmentManager, lc : Lifecycle) : FragmentStateAdapter(fm,lc) {
    private val productFrag = ReportProductWarehouseFragment()
    private val materialFrag = ReportMaterialWarehouseFragment()

    override fun getItemCount(): Int {
        return tabFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position] as Fragment
    }

    private val listPage: Map<Int, Fragment> = mapOf(
//        Pair(OVERVIEW_PAGE_INDEX, planFragment),
        Pair(PRODUCT_PAGE_INDEX, productFrag),
        Pair(MATERIAL_PAGE_INDEX, materialFrag),
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