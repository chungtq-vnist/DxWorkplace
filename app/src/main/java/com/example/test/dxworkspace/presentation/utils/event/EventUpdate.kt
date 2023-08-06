package com.example.test.dxworkspace.presentation.utils.event

class EventUpdate(val type: Int, var value: Any? = null) : EventBus.Event() {

    companion object {
        const val UPDATE_TIME_FILTER_HOME = 1
        const val UPDATE_TIMER = 2
        const val UPDATE_DASHBOARD_MANUFACTURING = 3
        const val UPDATE_LISTWORK_DASHBOARD_MANUFACTURING = 4
        const val UPDATE_ROLE = 5
        const val UPDATE_LIST_USER = 6
        const val UPDATE_COMMAND = 7
        const val UPDATE_LOT = 8
        const val UPDATE_REQUEST = 9
        const val UPDATE_PLAN = 10
        const val UPDATE_TIMESHEET = 11
        const val UPDATE_COUNT_NOTIFY = 12
        const val SYNC_DASHBOARD_MANUFACTURING = 6
        const val SYNC_DASHBOARD_INVENTORY = 7
        const val SYNC_DASHBOARD_FINANCIAL = 8
    }
}