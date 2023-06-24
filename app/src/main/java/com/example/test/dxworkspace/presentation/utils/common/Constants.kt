package com.example.test.dxworkspace.presentation.utils.common

object Constants {
    object APLogin{
        const val SHARE_PREFERENCES = "SHARE_PREFERENCES"
        const val IS_LOGGED = "IS_LOGGED"
        const val TOKEN = "TOKEN"
        const val CURRENT_ROLE = "CURRENT_ROLE"
        const val CURRENT_ROLE_ID = "CURRENT_ROLE_ID"
        const val CURRENT_PAGE = "CURRENT_PAGE"
        const val CURRENT_USER = "CURRENT_USER"
        const val USER_PROFILE = "USER_PROFILE"
        const val LOGIN_RESPONSE_INFO = "LOGIN_RES_INFO"
        const val DB_NAME = "DB_NAME"
    }

    object STATE_WORK {
        const val STATE_WORK_NOW ="STATE_WORK_NOW"
        const val STATE_WORK_NEXT = "STATE_WORK_NEXT"
        const val STATE_NONE ="NONE" // dang ko lam gi
        const val STATE_SYNC = "SYNC" // dang dong bo du lieu
        const val STATE_WAIT_SYNC ="WAIT_SYNC" // dang cho de dong bo du lieu
    }

    object VERSION_KEY {
        const val ROLE ="role"
        const val USER ="user"
        const val USER_ROLE = "userrole"
        const val COMPONENT ="component"
        const val LINK = "link"
        const val PRIVILEGE = "privilege"
        const val MANUFACTURINGWORK = "manufacturingWork"
    }

    const val IS_COUNTING = "IS_COUNTING"
    const val TASK_ID_COUNTING = "TASK_ID_COUNTING"
    const val TIMERID_COUNTING = "TIMERID__COUNTING"
    const val START_TIME_COUNT = "START_TIME_COUNT"
    const val END_TIME_COUNT = "END_TIME_COUNT"

    object DatePicker {
        val START_TIME: String by lazyOf("START_TIME")
        val END_TIME: String by lazyOf("END_TIME")
        val QUICK_TODAY: String by lazyOf("QUICK_TODAY")
        val QUICK_YESTERDAY: String by lazyOf("QUICK_YESTERDAY")
        val COMPARE_PRE_DAY: String by lazyOf("COMPARE_PRE_DAY")
        val COMPARE_SAME_WEEKDAY: String by lazyOf("COMPARE_SAME_WEEKDAY")

        val QUICK_THIS_WEEK: String by lazyOf("QUICK_THIS_WEEK")
        val QUICK_PRE_WEEK: String by lazyOf("QUICK_PRE_WEEK")
        val QUICK_7_DAY: String by lazyOf("QUICK_7_DAY")
        val COMPARE_PERIOD_WEEK: String by lazyOf("COMPARE_PERIOD_WEEK")
        val COMPARE_4_WEEKDAY: String by lazyOf("COMPARE_4_WEEKDAY")

        val QUICK_THIS_MONTH: String by lazyOf("QUICK_THIS_MONTH")
        val QUICK_PRE_MONTH: String by lazyOf("QUICK_PRE_MONTH")
        val QUICK_30_DAY: String by lazyOf("QUICK_30_DAY")
        val COMPARE_PERIOD_MONTH: String by lazyOf("COMPARE_PERIOD_MONTH")
        val COMPARE_SAME_MONTH: String by lazyOf("COMPARE_SAME_MONTH")

        val QUICK_365_DAY: String by lazyOf("QUICK_365_DAY")
        val QUICK_THIS_YEAR: String by lazyOf("QUICK_THIS_YEAR")
        val QUICK_PRE_YEAR: String by lazyOf("QUICK_PRE_YEAR")
        val COMPARE_PERIOD_YEAR: String by lazyOf("COMPARE_PERIOD_YEAR")

        val OTHER: String by lazyOf("OTHER")
    }
}