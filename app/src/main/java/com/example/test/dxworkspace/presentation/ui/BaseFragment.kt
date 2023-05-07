package com.example.test.dxworkspace.presentation.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment< V : ViewDataBinding> : DaggerFragment() , ViewTreeObserver.OnGlobalLayoutListener {
    var mActivity: Activity? = null

    private var rootView: View? = null
    var binding: V? = null

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var gson: Gson

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layoutId = getLayoutId()
        if (rootView != null) {
            val parent = rootView!!.parent as ViewGroup?
            parent?.removeView(rootView)
        } else {
            try {
                binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
                rootView = if (binding != null) {
                    binding!!.root
                } else {
                    inflater.inflate(layoutId, container, false)
                }
                rootView!!.viewTreeObserver.addOnGlobalLayoutListener(this)
            } catch (e: InflateException) {
                e.printStackTrace()
            }
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true
        view.isFocusable = true
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
    }

    @SuppressLint("MissingPermission")
    fun Context.hasConnect(): Boolean {
        var result = false // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = this.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || this.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    ) || this.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                }
            }
        } else {
            cm?.run {
                result = cm.activeNetworkInfo?.isConnected ?: false
            }
        }

        return result
    }

    override fun onGlobalLayout() {
        rootView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
    }

//    internal fun popToBackStack() {
//
//        if (activity is BaseActivity<*>) {
//            (activity as BaseActivity<*>).popToBackStack()
//        }
//    }

//    fun popTo(clazz: Class<*>) {
//        if (activity is BaseActivity<*>) {
//            (activity as BaseActivity<*>).popTo(clazz)
//        }
//    }

    protected fun popToInclusive(clazz: Class<*>) {
        if (activity is BaseActivity<*>) {
            (activity as BaseActivity<*>).popToInclusive(clazz)
        }
    }

    fun currentFragment(containerId: Int) = if (activity is BaseActivity<*>) {
        (activity as BaseActivity<*>).currentFragment(containerId)
    } else null

}