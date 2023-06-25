package com.example.test.dxworkspace.data.di

import android.app.Application
import android.content.SharedPreferences
import com.example.test.dxworkspace.data.local.preferences.AppPreferences
import com.example.test.dxworkspace.data.repository.*
import com.example.test.dxworkspace.domain.repository.*
import com.example.test.dxworkspace.presentation.utils.common.Constants
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(application: Application): SharedPreferences =
        AppPreferences.customPrefs(application, Constants.APLogin.SHARE_PREFERENCES)

    @Provides
    @Singleton
    fun providesAuthRepository(repository : AuthRepositoryImpl) : AuthRepository = repository

    @Provides
    @Singleton
    fun providesComponentRepository(repository : ComponentRepositoryImpl) : ComponentRepository = repository


    @Provides
    @Singleton
    fun providesConfigRepository(repository : ConfigRepositoryImpl) : ConfigRepository = repository


    @Provides
    @Singleton
    fun providesVersionRepository(repository : VersionRepositoryImpl) : VersionRepository = repository

    @Provides
    @Singleton
    fun providesTaskRepository(repository : TaskRepositoryImpl) : TaskRepository = repository

    @Provides
    @Singleton
    fun providesManufacturingWorkRepository(repository : ManufacturingWorkRepositoryImpl) : ManufacturingWorkRepository = repository

    @Provides
    @Singleton
    fun providesDashboardManufacturingRepository(repository : DashboardManufacturingRepositoryImpl) : DashboardManufacturingRepository = repository

    @Provides
    @Singleton
    fun providesReportRepository(repository : ReportRepositoryImpl) : ReportRepository = repository

}