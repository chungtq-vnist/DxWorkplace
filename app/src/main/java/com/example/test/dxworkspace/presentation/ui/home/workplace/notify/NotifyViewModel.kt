package com.example.test.dxworkspace.presentation.ui.home.workplace.notify

import androidx.lifecycle.MutableLiveData
import com.example.test.dxworkspace.data.entity.notify.PaginateNotificationsResponse
import com.example.test.dxworkspace.data.entity.notify.ParamGetPageNotify
import com.example.test.dxworkspace.data.entity.notify.ParamMarkNotifyReaded
import com.example.test.dxworkspace.domain.usecase.notify.GetPaginateNotificationUseCase
import com.example.test.dxworkspace.domain.usecase.notify.MarkReadedNotificationUseCase
import com.example.test.dxworkspace.presentation.ui.BaseViewModel
import javax.inject.Inject

class NotifyViewModel @Inject constructor(
    private val getPaginateNotificationUseCase: GetPaginateNotificationUseCase,
    private val markReadedNotificationUseCase: MarkReadedNotificationUseCase,

) : BaseViewModel() {

    val notifyResponse = MutableLiveData<PaginateNotificationsResponse>()
    val statusGetNotify = MutableLiveData<Boolean>()
    val markReaded = MutableLiveData<Boolean>()
    val markReadedAll = MutableLiveData<Boolean>()
    fun getNotifications(data : ParamGetPageNotify,isShowLoading:Boolean = false){
        showLoading(isShowLoading)
        getPaginateNotificationUseCase(data){
            it.either({
                showLoading(false)
                statusGetNotify.value = false
            },
                {
                    showLoading(false)
                    statusGetNotify.value = true
                    notifyResponse.value = it
                })
        }
    }

    fun markReaded(d : ParamMarkNotifyReaded){
        markReadedNotificationUseCase(d){
           it.either({

           },{
               markReaded.value = true
           })
        }
    }

    fun markReadedAll(d : ParamMarkNotifyReaded){
        markReadedNotificationUseCase(d){
           it.either({

           },{
               markReadedAll.value = true
           })
        }
    }
}