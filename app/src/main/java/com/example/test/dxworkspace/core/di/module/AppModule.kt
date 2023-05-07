package com.example.test.dxworkspace.core.di.module

import android.app.Application
import android.content.Context
import com.example.test.dxworkspace.data.di.NetworkModule
import com.example.test.dxworkspace.data.di.RepositoryModule
import com.example.test.dxworkspace.presentation.di.ActivityBuildersModule
import com.example.test.dxworkspace.presentation.di.FragmentBuildersModule
import com.example.test.dxworkspace.presentation.di.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [FragmentBuildersModule::class, ActivityBuildersModule::class, ViewModelModule::class, RepositoryModule::class, NetworkModule::class]
)
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application
}