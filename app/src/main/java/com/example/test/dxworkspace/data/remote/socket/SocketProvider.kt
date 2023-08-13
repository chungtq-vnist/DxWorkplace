package com.example.test.dxworkspace.data.remote.socket

import android.util.Log
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventSyncMessage
import com.example.test.dxworkspace.presentation.utils.event.EventUpdate
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketProvider @Inject constructor() {
    companion object {
//        val urlSocket = "http://192.168.1.3:8000"
        val urlSocket = "http://119.17.214.105:9000"
        var mSocket: Socket? = null
    }

    @Synchronized
    fun setSocket(userId : String) {
        try {
            val options = IO.Options()
            options.query = "userId=${userId}"
            mSocket = IO.socket(urlSocket,options)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getSocket() =
        if (mSocket != null) mSocket
        else {
//            setSocket()
            mSocket
        }

    @Synchronized
    fun establishConnection() {
        mSocket?.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket?.disconnect()
        mSocket?.off(Socket.EVENT_CONNECT){
            Log.d("SOCKET" , "socket connected")
        }
        mSocket?.off(Socket.EVENT_CONNECT_ERROR){
            Log.d("SOCKET" , "socket connect error")
        }
        mSocket?.off(Socket.EVENT_DISCONNECT){
            Log.d("SOCKET" , "socket disconnect")
        }
        mSocket?.off("test_socket") { data ->
            if(data[0] != null) {
                Log.d("KGM","content socket : ${data[0]}")
            }

        }
    }

    fun subscribeTopic() {
        if (mSocket != null ) {
            mSocket!!.on(Socket.EVENT_CONNECT){
                Log.d("SOCKET" , "socket connected")
            }
            mSocket!!.on(Socket.EVENT_CONNECT_ERROR){
                Log.d("SOCKET" , "socket connect error")
            }
            mSocket!!.on(Socket.EVENT_DISCONNECT){
                Log.d("SOCKET" , "socket disconnect")
            }
            mSocket!!.on("test_socket") { data ->
                if(data[0] != null) {
                    Log.d("SOCKET","content socket : ${data[0]}")
                }

            }
            mSocket!!.on("versions_update") { data ->
                    Log.d("SOCKET","versions_update  ")
//                org.greenrobot.eventbus.EventBus.getDefault().post(EventSyncMessage(EventSyncMessage.SYNC_CHECK_VERSION))

                EventBus.getDefault().post(EventSyncMessage(EventSyncMessage.SYNC_CHECK_VERSION))
            }
            mSocket!!.on("stop timers"){
                EventBus.getDefault().post(EventSyncMessage(EventSyncMessage.SYNC_TIMESHEET_LOG))
            }
            mSocket!!.on("dashboard_update"){data ->
                Log.d("SOCKET","dashboard update")
                if( data.firstOrNull() is String && (data.firstOrNull() as String) == "manufacturing-control") EventBus.getDefault().post(EventUpdate(EventUpdate.SYNC_DASHBOARD_MANUFACTURING))
                if( data.firstOrNull() is String && (data.firstOrNull() as String) == "inventory")
                    EventBus.getDefault().post(EventUpdate(EventUpdate.SYNC_DASHBOARD_INVENTORY))
                if( data.firstOrNull() is String && (data.firstOrNull() as String) == "financial")
                    EventBus.getDefault().post(EventUpdate(EventUpdate.SYNC_DASHBOARD_FINANCIAL))

            }
        }
    }
}