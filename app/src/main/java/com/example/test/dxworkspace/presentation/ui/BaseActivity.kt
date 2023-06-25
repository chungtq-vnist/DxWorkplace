package com.example.test.dxworkspace.presentation.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.core.extensions.justTry
import com.example.test.dxworkspace.core.extensions.observe
import com.example.test.dxworkspace.presentation.ui.dialog.DialogProcess
import com.example.test.dxworkspace.presentation.ui.widgets.Boast
import com.example.test.dxworkspace.presentation.utils.event.EventBus
import com.example.test.dxworkspace.presentation.utils.event.EventSyncMessage
import com.example.test.dxworkspace.presentation.utils.event.EventToast
import com.google.gson.Gson
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<V : ViewDataBinding> : DaggerAppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var gson: Gson

    lateinit var binding: V

    private var dialogProcess: DialogProcess? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun updateUI(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        updateUI(savedInstanceState)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 1) {
            finish()
            // implement check an 2 lan back moi close app
        } else {
            super.onBackPressed()
        }
    }

    protected fun <VM : BaseViewModel> observeLoading(viewModel: VM) {
        observe(viewModel.isLoading, {
            if (it == true) {
                showDialogProcess()
            } else {
                hiddenDialogProcess()
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // ham bat su kien tu broadcast receiver
//    fun onBusEvent(event: EventSyncMessage) {
//
//    }

    @Throws
    open fun openFragment(
        resId: Int,
        fragmentClazz: Class<*>,
        args: Bundle? = null,
        addBackStack: Boolean = true
    ) {
        val tag = fragmentClazz.simpleName
        try {
            val isExisted = try {
                supportFragmentManager.popBackStackImmediate(tag, 0)    // IllegalStateException
            } catch (e: IllegalStateException) {
                false
            }
            if (!isExisted) {
                val fragment: Fragment
                try {
                    fragment =
                        (fragmentClazz as Class<Fragment>).newInstance().apply { arguments = args }

                    val transaction = supportFragmentManager.beginTransaction()
                    //transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.add(resId, fragment, tag)

                    if (addBackStack) {
                        transaction.addToBackStack(tag)
                    }
                    transaction.commitAllowingStateLoss()

                } catch (e: InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun popToInclusive(clazz: Class<*>) {
        justTry {
            supportFragmentManager.popBackStack(
                clazz.simpleName,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    fun isCurrentFragment(java: Class<*>, containerId: Int): Boolean {
        val curr = supportFragmentManager.findFragmentById(containerId) ?: return false
        return curr::class.java == java
    }

    fun currentFragment(containerId: Int) = supportFragmentManager.findFragmentById(containerId)

    fun showToast(event: EventToast) {
        val inflater = layoutInflater

        val layout =
            inflater.inflate(R.layout.custom_toast, this.findViewById(R.id.custom_toast_container))
        val text = layout.findViewById<TextView>(R.id.text)
        if (event.textId != 0)
            text.setText(event.textId)
        else text.text = event.text

        layout.setBackgroundResource(if (event.isFail) R.drawable.round_corner_error else R.drawable.round_corner_success)
        Boast.makeCustom(this, layout).show(cancelCurrent = true)
    }

    fun showDialogProcess() {
        justTry {
            if (dialogProcess == null) {
                dialogProcess = DialogProcess(this)
            }
            dialogProcess?.show()
        }
    }


    fun hiddenDialogProcess() {
        dialogProcess?.dismiss()
    }

    fun hideKeyboard(){
        com.example.test.dxworkspace.presentation.utils.common.hideKeyboard(this)
    }
}