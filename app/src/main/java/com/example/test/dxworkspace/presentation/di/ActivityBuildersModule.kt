package com.example.test.dxworkspace.presentation.di

import com.example.test.dxworkspace.presentation.ui.home.HomeActivity
import com.example.test.dxworkspace.presentation.ui.login.LoginActivity
import com.example.test.dxworkspace.presentation.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity() : LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity() : SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity() : HomeActivity
}