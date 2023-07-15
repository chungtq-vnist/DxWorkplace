package com.example.test.dxworkspace.service

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.test.dxworkspace.data.entity.notify.AssociatedDataObject
import com.example.test.dxworkspace.data.entity.notify.NotificationModel
import com.example.test.dxworkspace.presentation.model.NotificationFirebaseModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import io.reactivex.disposables.Disposable
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class FirebaseMessageService : FirebaseMessagingService() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    var gson: Gson = Gson()

    private val notificationManagerBv = NotificationManager(this)
//    private val viewModel by inject<NotifyViewModel>()


//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//
//        try {
//            LogHelper.error("onMessageReceived")
//            if (remoteMessage.data.isNotEmpty()) {
//                LogHelper.error("Message data payload: " + remoteMessage.data)
//            }
//
//            if (remoteMessage.notification != null) {
//                LogHelper.error("Message Notification Body: " + remoteMessage.notification!!.body)
//            }
//            LogHelper.error("Message data payload: " + remoteMessage.getNotification())
//
////            if (!isLogin()) return
////            val notifyRaw = NotifyRaw("Test", "remoteMessage.data.toString()")
////            val payload = remoteMessage.data["body"]
//
//            val payloadData = gson.fromJson(getJsonFromMap(remoteMessage.data.toMap()).toString(), NotificationRaw::class.java)
////            if (AppPreferences.store.store.serverId == payloadData.storeId) {
//
//            Log.v("DEBUG","store id : ${payloadData.storeId}")
//            Log.v("DEBUG","store id : ${AppPreferences.store.storeId()}")
//            if(remoteMessage.data.get("storeId") == AppPreferences.store.storeId().toString())
//            {
//                handleNotification(payloadData)
//            }
//
//
////            if (!isLogin()) return
////            addDispose(viewModel.saveNotification(payloadData) {
////                handleNotification(payloadData)
////            })
////            }
////            LogHelper.error(payloadData.toString())
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }


    override fun handleIntent(intent: Intent) {
        Log.d("KGMM","haizzz handle intent noti")
        if(isNotificationIntent(intent)) {
            val bundle = intent.extras
            val mapData = mutableMapOf<String, Any>()
            if (bundle != null) {
                for (key in bundle.keySet()) {
                    val value = bundle[key]
                    if(value != null) mapData[key] = value
                    Log.d("FCM", "Key: $key Value: $value")
                }
            }
            val payloadData = gson.fromJson(getJsonFromMap(mapData).toString(), NotificationFirebaseModel::class.java)
//        if(bundle?.get("storeId") == AppPreferences.store.storeId().toString())
//        {
            val t = NotificationModel(
                AssociatedDataObject((if(payloadData.type == "") "0" else payloadData.type ?: "0").toInt(), payloadData.body ?: ""),
                false, payloadData.title ?: "", payloadData.level ?: "", payloadData?.body ?: "",
                payloadData?.sender ?: ""
            )
            handleNotification(t)
        } else {
            super.handleIntent(intent)
        }


    }

    private fun isNotificationIntent(intent: Intent): Boolean {
        return intent.hasExtra("google.message_id")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        AppPreferences.tokenFirebase = ""
//        LogHelper.error("new token $token")
    }

    private fun handleNotification(notify: NotificationModel) {
//        postNormal(EventUpdate(UPDATE_NOTIFICATION))
            notificationManagerBv.customNotification(
                notify.title,
                notify.content,
                notify.id,
                 notify.associatedDataObject?.dataType,
                "",
            )

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

@Throws(JSONException::class)
private fun getJsonFromMap(map: Map<String, Any>): JSONObject {
    val jsonData = JSONObject()
    for (key in map.keys) {
        var value = map[key]
        if (value is Map<*, *>) {
            value = getJsonFromMap(value as Map<String, Any>)
        }
        jsonData.put(key, value)
    }
    return jsonData
}