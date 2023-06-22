package com.example.test.dxworkspace.presentation.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.test.dxworkspace.DxApplication
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.di.viewmodel.ViewModelFactory
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.core.extensions.viewModel
import com.example.test.dxworkspace.data.remote.socket.SocketProvider
import com.example.test.dxworkspace.databinding.ActivityHomeBinding
import com.example.test.dxworkspace.domain.repository.ConfigRepository
import com.example.test.dxworkspace.presentation.ui.BaseActivity
import com.example.test.dxworkspace.presentation.ui.home.workplace.WorkplaceFragment
import com.example.test.dxworkspace.presentation.utils.common.Constants
import com.example.test.dxworkspace.presentation.utils.common.registerGreen
import com.example.test.dxworkspace.presentation.utils.common.unregisterGreen
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventNextHome
import com.example.test.dxworkspace.presentation.utils.event.EventSyncMessage
import io.socket.client.Socket
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var viewModel :HomeViewModel

    @Inject
    lateinit var socketProvider: SocketProvider

    @Inject
    lateinit var configRepository: ConfigRepository

    var checkVersionRetry = 0 // số lần retry lại check version khi bị lỗi , tối đa 3 lần



    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModel(viewModelFactory){
            // observer

        }
        if(::socketProvider.isInitialized){
            justTry {
                socketProvider.setSocket(configRepository.getUser().id)
                socketProvider.subscribeTopic()
                socketProvider.establishConnection()
            }
        }


    }

    override fun updateUI(savedInstanceState: Bundle?) {
        DxApplication.getInstance().observerLifecycle()
        openFragment(
            R.id.containerHome,
            WorkplaceFragment::class.java,

        )
        checkVersion()
//        viewModel.checkVersion {
//            EventBus.getDefault().post(EventSyncMessage(EventSyncMessage.SYNC_FIRST))
//        }

    }

    @Subscribe
    fun onEventNextHome(eventNextHome: EventNextHome) {
        if (isCurrentFragment(eventNextHome.clazz, R.id.containerHome)) return
        openFragment(
            R.id.containerHome,
            eventNextHome.clazz,
            eventNextHome.bundle,
            eventNextHome.isAddToBackStack
        )
    }

    fun onBusEvent(event: EventSyncMessage){
        when (event.type) {
            EventSyncMessage.SYNC_CHECK_VERSION -> {
                DxApplication.stateWorkNext = Constants.STATE_WORK.STATE_WAIT_SYNC
                checkVersion()
            }

            else -> {}
        }
    }

    override fun onStart() {
        super.onStart()
        registerGreen()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        unregisterGreen()
        EventBus.getDefault().unregister(this)

    }

    fun checkVersion(){
        if(DxApplication.stateWork == Constants.STATE_WORK.STATE_SYNC) DxApplication.stateWorkNext = Constants.STATE_WORK.STATE_WAIT_SYNC
        else {
            DxApplication.stateWork = Constants.STATE_WORK.STATE_SYNC
            DxApplication.stateWorkNext = Constants.STATE_WORK.STATE_NONE
            viewModel.checkVersion({
                if(it){
                    DxApplication.stateWork = Constants.STATE_WORK.STATE_NONE
                    checkVersionRetry = 0
                    if(DxApplication.stateWorkNext == Constants.STATE_WORK.STATE_WAIT_SYNC) checkVersion()
                } else {
                    DxApplication.stateWork = Constants.STATE_WORK.STATE_NONE
                    checkVersionRetry++
                    if(checkVersionRetry <= 3)  checkVersion()
                }
            } , {
                checkVersionRetry++
                if(checkVersionRetry <= 3) checkVersion()
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socketProvider.closeConnection()
    }
}