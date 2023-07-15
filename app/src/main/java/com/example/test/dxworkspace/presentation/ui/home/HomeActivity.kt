package com.example.test.dxworkspace.presentation.ui.home

import android.content.Intent
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
import com.example.test.dxworkspace.presentation.utils.event.EventGoToNotification
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

    private var notificationId = ""
    private var notificationType = 0
    private var notificationObjectId = ""
    private var notificationSubTitle = ""
    private var notificationHeader = ""


    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DxApplication.appIsRunning = true
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

        // receiver from notify
        notificationId = intent?.extras?.getString("NOTIFY_ID", "") ?: ""
        notificationType = intent?.extras?.getInt("DATA_TYPE", 0) ?: 0
        notificationObjectId = intent?.extras?.getString("OBJECT_ID", "") ?: ""
        notificationSubTitle = intent?.extras?.getString("SUB_TITLE", "") ?: ""
        notificationHeader = intent?.extras?.getString("HEADER", "") ?: ""


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        notificationId = intent?.extras?.getString("NOTIFY_ID", "") ?: ""
        notificationType = intent?.extras?.getInt("DATA_TYPE", 0) ?: 0
        notificationObjectId = intent?.extras?.getString("OBJECT_ID", "") ?: ""
        notificationSubTitle = intent?.extras?.getString("SUB_TITLE", "") ?: ""
        notificationHeader = intent?.extras?.getString("HEADER", "") ?: ""
        goToScreenFromNotify()
    }

    fun goToScreenFromNotify(){
        when(notificationType){
            1 -> {
                EventBus.getDefault().post(EventGoToNotification(EventGoToNotification.DETAIL_TASK))
            }
            5 -> {
                EventBus.getDefault().post(EventGoToNotification(EventGoToNotification.DETAIL_REQUEST))
            }
            else -> {
                EventBus.getDefault().post(EventGoToNotification(EventGoToNotification.DIALOG_DETAIL,
                    listOf(notificationHeader,notificationSubTitle)))
            }
        }
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        DxApplication.getInstance().observerLifecycle()
        openFragment(
            R.id.containerHome,
            WorkplaceFragment::class.java,

        )
        DxApplication.stateWork = Constants.STATE_WORK.STATE_NONE
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
        justTry {
            viewModel.getTaskTimeSheetLog()
        }
    }

    fun onBusEvent(event: EventSyncMessage){
        when (event.type) {
            EventSyncMessage.SYNC_CHECK_VERSION -> {
                DxApplication.stateWorkNext = Constants.STATE_WORK.STATE_WAIT_SYNC
                checkVersion()
            }
            EventSyncMessage.SYNC_TIMESHEET_LOG -> {
                viewModel.getTaskTimeSheetLog()
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
        DxApplication.appIsRunning = false
        socketProvider.closeConnection()
    }
}