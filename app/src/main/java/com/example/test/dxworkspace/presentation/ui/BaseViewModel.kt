package com.example.test.dxworkspace.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test.dxworkspace.core.exception.Failure

abstract class BaseViewModel : ViewModel() {
    var failure: MutableLiveData<Failure> = MutableLiveData()
    val isLoading = MutableLiveData<Boolean>()
    fun showLoading(isShow: Boolean = true) {
        isLoading.value = isShow
    }

    open fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}