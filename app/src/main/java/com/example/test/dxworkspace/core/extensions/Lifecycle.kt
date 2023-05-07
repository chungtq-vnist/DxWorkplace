package com.example.test.dxworkspace.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.test.dxworkspace.core.exception.Failure

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, observer: Observer<T?>) =
        liveData.observe(this, observer)

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
        liveData.observe(this, Observer(body))

fun <L : LiveData<Failure>> LifecycleOwner.failure(liveData: L, body: (Failure?) -> Unit) =
        liveData.observe(this, Observer(body))

fun <T : Any, L : LiveData<T>> LifecycleOwner.unObserve(liveData: L, observer: Observer<T?>): () -> Unit = {
    liveData.removeObserver(observer)
//    liveData.removeObservers(this)
}
