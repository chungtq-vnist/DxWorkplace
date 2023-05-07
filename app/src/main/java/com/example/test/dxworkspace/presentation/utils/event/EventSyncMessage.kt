package com.example.test.dxworkspace.presentation.utils.event

class EventSyncMessage(val type: Int, var message: List<String>? = null) : EventBus.Event() {

    companion object {
        const val SYNC_CART = 1
        const val SYNC_AREAS = 2
        const val SYNC_TABLES = 3
        const val SYNC_MENU = 4
        const val SYNC_CATEGORY = 5
        const val SYNC_ITEM = 6
        const val SYNC_MOD_SET = 7
        const val SYNC_PAYMENT_METHOD = 8
        const val SYNC_DISCOUNT = 9
        const val SYNC_ORDER = 10
        const val SYNC_FIRST = 11
        const val SYNC_STAFF_DELETE = 12
        const val SYNC_STAFF = 13
        const val SYNC_SETTING = 14
        const val SYNC_STORE = 15
        const val SYNC_UNABLE_CONNECT_PRINT = 16
        const val SYNC_ACTIVE_DEVICE = 17
        const val SYNC_CHANGE_MANAGE_TABLE = 18
        const val SYNC_SETTING_OFFLINE = 19
        const val SYNC_CHANGE_PERMISSION_STAFF = 20
        const val SYNC_SETTING_PRINT = 21
        const val SYNC_MERGE_SUCCESS = 22
        const val SYNC_UNAUTHORIZED = 23
        const val SYNC_CONNECT_NETWORK = 24
        const val SYNC_CHECK_VERSION = 25
        const val SYNC_EXPIRED = 26
        const val SYNC_SERVICE_FEE = 27
        const val SYNC_CHANGE_INGREDIENT = 29
        const val SYNC_EXPORT_DATA = 30
        const val SYNC_KITCHEN = 32
        const val SYNC_INIT_KITCHEN = 33
        const val SYNC_CHANGE_MONEY = 34
        const val SYNC_BLOCK_SITE = 35

        //        const val SYNC_TAX_FEE = 27
        const val SYNC_TAX_FEE = 36
        const val SYNC_SHIPPING_METHOD = 37
        const val SYNC_ITEM_IN_MENU = 38
        const val SYNC_COMBO = 39
        const val SYNC_CHANGE_SHIFT_MANAGEMENT = 40
        const val SYNC_UPDATE_CHANGE_SHIFT_MANAGEMENT = 41
        const val SYNC_BOOKING = 42
        const val SYNC_CART_CHANGE_OTHER_DEVICE = 43
        const val SYNC_CART_LOCAL = 44
        const val SYNC_EXPIRE_CASH = 45
        const val SYNC_ACTIVE_CODE = 46
        const val SYNC_RETRY_SERVER = 47
        const val SYNC_TAX_DIALOG = 48
        const val SYNC_FEE_DIALOG = 49
        const val SYNC_KITCHEN_RECEIPT = 50
        const val SYNC_REMOVE_KITCHEN_RECEIPT = 51
        const val SYNC_KITCHEN_SETTING = 52
        const val SYNC_MESSAGE = 53
        const val SYNC_CART_ERROR = 54
        const val SYNC_FILE_ERROR = 55
        const val SYNC_QR_ORDER = 56
        const val SYNC_MANAGE_QR_ORDER = 57
        const val SYNC_CART_QR_ORDER = 58
        const val SYNC_DEFAULT_ITEM = 59
        const val SYNC_AGAIN_HOST = 60 // sync lai bang cart , order , table booking
    }
}