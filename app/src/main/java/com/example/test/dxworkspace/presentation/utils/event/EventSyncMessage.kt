package com.example.test.dxworkspace.presentation.utils.event

class EventSyncMessage(val type: Int, var message: List<String>? = null) : EventBus.Event() {

    companion object {
//        const val SYNC_CART = 1
//        const val SYNC_AREAS = 2
//        const val SYNC_TABLES = 3
//        const val SYNC_MENU = 4
//        const val SYNC_CATEGORY = 5
//        const val SYNC_ITEM = 6
//        const val SYNC_MOD_SET = 7
//        const val SYNC_PAYMENT_METHOD = 8
//        const val SYNC_DISCOUNT = 9
//        const val SYNC_ORDER = 10
//        const val SYNC_FIRST = 11
//        const val SYNC_STAFF_DELETE = 12
//        const val SYNC_STAFF = 13
//        const val SYNC_SETTING = 14
//        const val SYNC_STORE = 15
//        const val SYNC_UNABLE_CONNECT_PRINT = 16
//        const val SYNC_ACTIVE_DEVICE = 17
//        const val SYNC_CHANGE_MANAGE_TABLE = 18
//        const val SYNC_SETTING_OFFLINE = 19
//        const val SYNC_CHANGE_PERMISSION_STAFF = 20
//        const val SYNC_SETTING_PRINT = 21
//        const val SYNC_MERGE_SUCCESS = 22
        const val SYNC_UNAUTHORIZED = 23
        const val SYNC_CONNECT_NETWORK = 24
        const val SYNC_CHECK_VERSION = 4

        const val SYNC_FIRST = 1
        const val SYNC_PRIVILEGE = 2
        const val SYNC_USER = 3
        const val SYNC_MANUFACTURING_WORK = 4
        const val SYNC_TIMESHEET_LOG = 5
    }
}