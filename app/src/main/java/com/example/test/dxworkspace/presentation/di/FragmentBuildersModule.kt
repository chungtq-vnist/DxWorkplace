package com.example.test.dxworkspace.presentation.di

import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingCommandFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingPlanFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingRequestFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog.RangeDateSelectFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.TimeSheetFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.WorkplaceFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.detail_task.DetailTaskFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.time_picker.RangeMonthFragment
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
    abstract fun contributeDashboardManufacturingPlanFragment(): DashboardManufacturingPlanFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardManufacturingCommandFragment(): DashboardManufacturingCommandFragment

    @ContributesAndroidInjector
    abstract fun contributeDashboardManufacturingRequestFragment(): DashboardManufacturingRequestFragment

    @ContributesAndroidInjector
    abstract fun contributeRangeDateSelectFragment(): RangeDateSelectFragment
}