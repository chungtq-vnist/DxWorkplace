package com.example.test.dxworkspace.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.core.di.viewmodel.ViewModelFactory
import com.example.test.dxworkspace.core.di.viewmodel.ViewModelKey
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.dashboard.control.DashboardControlManufacturingViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.mill.ManufacturingMillViewModel
import com.example.test.dxworkspace.presentation.ui.home.manufacturing.works.ManufacturingWorkViewModel
import com.example.test.dxworkspace.presentation.ui.home.report.ReportViewModel
import com.example.test.dxworkspace.presentation.ui.home.workplace.WorkplaceViewModel
import com.example.test.dxworkspace.presentation.ui.home.workplace.create_task.CreateTaskViewModel
import com.example.test.dxworkspace.presentation.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindsViewModelFactory(viewmodelFactory : ViewModelFactory) :ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(homeViewModel: HomeViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindsLoginViewModel(loginViewModel: LoginViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WorkplaceViewModel::class)
    abstract fun bindsWorkplaceViewModel(workplaceViewModel: WorkplaceViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManufacturingWorkViewModel::class)
    abstract fun bindsManufacturingViewModel(workplaceViewModel: ManufacturingWorkViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardControlManufacturingViewModel::class)
    abstract fun bindsDashboardControlManufacturingViewModel(workplaceViewModel: DashboardControlManufacturingViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel::class)
    abstract fun bindsReportViewModel(workplaceViewModel: ReportViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManufacturingMillViewModel::class)
    abstract fun bindsManufacturingMillViewModel(workplaceViewModel: ManufacturingMillViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateTaskViewModel::class)
    abstract fun bindsCreateTaskViewModel(workplaceViewModel: CreateTaskViewModel) : ViewModel

}