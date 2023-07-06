package com.example.test.dxworkspace.presentation.di

import com.example.test.dxworkspace.presentation.ui.home.manufacturing.command.*
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingCommandFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingPlanFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardManufacturingRequestFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.dialog.RangeDateSelectFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.quality.DashboardQualityManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.ImportProductFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.ImportProductFragmentNew
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.ManufacturingLotDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.lot.ManufacturingLotFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill.ManufacturingMillDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill.ManufacturingMillFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.ManufacturingPlanDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.ManufacturingPlanFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.plan.create.CreateManufacturingPlanFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.CreateManufacturingRequestFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.request.ManufacturingRequestFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ChooseRoleFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkDetailFragment
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkFragment
import com.example.test.dxworkspace.presentation.ui.home.report.financial.ReportFinancialFragment
import com.example.test.dxworkspace.presentation.ui.home.report.manufacturing.ReportManufacturingFragment
import com.example.test.dxworkspace.presentation.ui.home.report.sale.ReportSaleFragment
import com.example.test.dxworkspace.presentation.ui.home.report.warehouse.ReportMaterialWarehouseFragment
import com.example.test.dxworkspace.presentation.ui.home.report.warehouse.ReportProductWarehouseFragment
import com.example.test.dxworkspace.presentation.ui.home.report.warehouse.ReportWarehouseFragment
import com.example.test.dxworkspace.presentation.ui.home.report.warehouse.ReportWasteWarehouseFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.ChooseUserFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.SelectRoleFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.TimeSheetFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.WorkplaceFragment
import com.example.test.dxworkspace.presentation.ui.home.workplace.create_task.CreateTaskFragment
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

    @ContributesAndroidInjector
    abstract fun contributeReportManufacturingFragment(): ReportManufacturingFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingWorkDetailFragment(): ManufacturingWorkDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseRoleFragment(): ChooseRoleFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingMillFragment(): ManufacturingMillFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingMillDetailFragment(): ManufacturingMillDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateTaskFragment(): CreateTaskFragment

    @ContributesAndroidInjector
    abstract fun contributeChooseUserFragment(): ChooseUserFragment

    @ContributesAndroidInjector
    abstract fun contributeReportWarehouseFragment(): ReportWarehouseFragment

    @ContributesAndroidInjector
    abstract fun contributeReportProductWarehouseFragment(): ReportProductWarehouseFragment

    @ContributesAndroidInjector
    abstract fun contributeReportMaterialWarehouseFragment(): ReportMaterialWarehouseFragment

    @ContributesAndroidInjector
    abstract fun contributeReportWasteWarehouseFragment(): ReportWasteWarehouseFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingCommandFragment(): ManufacturingCommandFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingPlanFragment(): ManufacturingPlanFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingCommandDetailFragment(): ManufacturingCommandDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingPlanDetailFragment(): ManufacturingPlanDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateManufacturingPlanFragment(): CreateManufacturingPlanFragment

    @ContributesAndroidInjector
    abstract fun contributeExportMaterialFragment(): ExportMaterialFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateLotFragment(): CreateLotFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingLotFragment(): ManufacturingLotFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingLotDetailFragment(): ManufacturingLotDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeImportProductFragment(): ImportProductFragment

    @ContributesAndroidInjector
    abstract fun contributeManufacturingRequestFragment(): ManufacturingRequestFragment

    @ContributesAndroidInjector
    abstract fun contributeCreateManufacturingRequestFragment(): CreateManufacturingRequestFragment

    @ContributesAndroidInjector
    abstract fun contributeImportProductFragmentNew(): ImportProductFragmentNew

    @ContributesAndroidInjector
    abstract fun contributeExportMaterialFragmentNew(): ExportMaterialFragmentNew
}



