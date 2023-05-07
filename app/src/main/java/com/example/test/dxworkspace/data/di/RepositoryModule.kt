package com.example.test.dxworkspace.data.di

import android.app.Application
import android.content.SharedPreferences
import com.example.test.dxworkspace.data.local.preferences.AppPreferences
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

}