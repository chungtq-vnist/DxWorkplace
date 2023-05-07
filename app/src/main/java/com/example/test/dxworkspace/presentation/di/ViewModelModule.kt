package com.example.test.dxworkspace.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.core.di.viewmodel.ViewModelFactory
import com.example.test.dxworkspace.core.di.viewmodel.ViewModelKey
import com.example.test.dxworkspace.presentation.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindsViewModelFactory(viewmodelFactory : ViewModelFactory) :ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindsHomeViewModel(homeViewModel: HomeViewModel) : ViewModel

}