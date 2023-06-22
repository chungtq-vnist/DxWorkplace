package com.example.test.dxworkspace.presentation.utils.event

import android.os.Bundle
import androidx.fragment.app.Fragment

class EventNextHome {

    var clazz: Class<*>
    var bundle: Bundle? = null
    var isAddToBackStack: Boolean = false

    constructor(clazz: Class<Fragment>, bundle: Bundle, addToBackStack: Boolean) {
        this.clazz = clazz
        this.bundle = bundle
        this.isAddToBackStack = addToBackStack
    }

    constructor(clazz: Class<*>, addToBackStack: Boolean) {
        this.clazz = clazz
        this.isAddToBackStack = addToBackStack
    }

    constructor(clazz: Class<*>, bundle: Bundle) {
        this.clazz = clazz
        this.bundle = bundle
        this.isAddToBackStack = true
    }

    constructor(clazz: Class<*>) {
        this.clazz = clazz
        this.isAddToBackStack = true
    }
}