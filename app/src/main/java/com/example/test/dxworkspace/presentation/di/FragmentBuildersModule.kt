package com.example.test.dxworkspace.presentation.di

import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingCommandFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingPlanFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingRequestFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog.RangeDateSelectFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.quality.DashboardQualityManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkFragment
import com.example.test.dxworkspace.presentation.ui.home.report.financial.ReportFinancialFragment
import com.example.test.dxworkspace.presentation.ui.home.report.sale.ReportSaleFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.SelectRoleFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.TimeSheetFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.WorkplaceFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.detail_task.DetailTaskFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.time_picker.RangeMonthFragment
import com.example.test.dxworkspace.presentation.ui.timepicker.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeWorkplaceFragment(): WorkplaceFragment

    @ContributesAndroidInjector
    abstract fun contributeMonthPickerFragment(): RangeMonthFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailTaskFragment(): DetailTaskFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingWorkFragment(): ManufacturingWorkFragment

    @ContributesAndroidInjector
    abstract fun contributeTimeSheetFragment(): TimeSheetFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardControlManufacturingFragment(): DashboardControlManufacturingFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardQualityManufacturingFragment(): DashboardQualityManufacturingFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardManufacturingPlanFragment(): DashboardManufacturingPlanFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardManufacturingCommandFragment(): DashboardManufacturingCommandFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardManufacturingRequestFragment(): DashboardManufacturingRequestFragment

    @ContributesAndroidInjector
    abstract fun contributeRangeDateSelectFragment(): RangeDateSelectFragment

    @ContributesAndroidInjector
    abstract fun contributeReportFinancialFragment(): ReportFinancialFragment

    @ContributesAndroidInjector
    abstract fun contributeRangeTimeSelectFragment(): RangeTimeSelectFragment

    @ContributesAndroidInjector
    abstract fun contributeDayFragment(): DayFragment

    @ContributesAndroidInjector
    abstract fun contributeRangeMonthFragment(): MonthFragment

    @ContributesAndroidInjector
    abstract fun contributeWeekFragment(): WeekFragment

    @ContributesAndroidInjector
    abstract fun contributeotherFragment(): OtherFragment

    @ContributesAndroidInjector
    abstract fun contributeSelectRoleFragment(): SelectRoleFragment

    @ContributesAndroidInjector
    abstract fun contributeReportSaleFragment(): ReportSaleFragment
}