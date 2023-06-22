package com.example.test.dxworkspace

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.test.dxworkspace.core.di.component.AppComponent
import com.example.test.dxworkspace.core.di.component.DaggerAppComponent
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventStateNetwork

import com.example.test.dxworkspace.presentation.utils.event.EventSyncMessage
import com.example.test.dxworkspace.presentation.utils.event.StateNetwork
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerApplication

class DxApplication : DaggerApplication(), HasAndroidInjector, LifecycleObserver {

//    @Inject
//    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }

//    override fun androidInjector(): AndroidInjector<Any> {
//        return androidInjector
//    }

    fun observerLifecycle() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun unObserverLifecycle() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }

    companion object {
        private lateinit var instance: DxApplication

        var stateWork = Constants.STATE_WORK.STATE_NONE
        var stateWorkNext = Constants.STATE_WORK.STATE_NONE

        @JvmStatic
        fun getInstance() = instance

        fun hasConnect(context: Context): Boolean {
            var result = false // Returns connection type. 0: none; 1: mobile data; 2: wifi
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = this.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || this.hasTransport(
                            NetworkCapabilities.TRANSPORT_WIFI) || this.hasTransport(
                            NetworkCapabilities.TRANSPORT_ETHERNET)
                    }
                }
            } else {
                cm?.run {
                    result = cm.activeNetworkInfo?.isConnected ?: false
                }
            }

            return result
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent.inject(this)

        val builder = NetworkRequest.Builder()
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        builder.addTransportType(NetworkCapabilities.TRANSPORT_VPN)

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                super.onUnavailable()
                checkNetWorkChanged()
                Log.v("AnhdtNetwork", "onUnavailable")
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                checkNetWorkChanged()
                Log.v("AnhdtNetwork", "onAvailable")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                checkNetWorkChanged()
                Log.v("AnhdtNetwork", "onLost")
            }

        }
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        cm?.registerNetworkCallback(builder.build(), callback)
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    fun checkNetWorkChanged() {
        val hasConnect = hasConnect(applicationContext)

        if (hasConnect) {
            EventBus.getDefault().post(EventSyncMessage(EventSyncMessage.SYNC_CONNECT_NETWORK))
        } else {
            EventBus.getDefault().post(EventStateNetwork(StateNetwork.UN_CONNECT))

//            handlerPostDelay({
//                socketManage.stopWork()
//                workerManage.stopWork()
//            }, 30)
        }
    }

}