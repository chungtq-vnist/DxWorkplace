package com.example.test.dxworkspace.presentation.utils.common

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dxworkspace.R
import com.example.test.dxworkspace.presentation.utils.common.decorator.LinearMarginDecoration
import java.text.NumberFormat
import java.util.*
import javax.inject.Qualifier


fun RecyclerView.addDefaultDecorator(margin: Int = resources.getDimensionPixelOffset(R.dimen._8sdp)) {
    addItemDecoration(
        LinearMarginDecoration(
            leftMargin = margin,
            topMargin = margin,
            rightMargin = margin,
            bottomMargin = margin,
            orientation = RecyclerView.VERTICAL
        )
    )
}

fun Dialog.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = Color.TRANSPARENT
            }
        }
    }
}
fun AlertDialog.initDialog(isCancelable: Boolean = false) {
    this.setCancelable(isCancelable)
    this.window?.let {
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}

fun TextView.setHtml(htmlText: String?) {
    text = if (htmlText != null) {
        HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        ""
    }
}
fun EditText.getTextz() = this.text?.toString()?.trim() ?: ""

fun EditText.clearText() = this.editableText.clear()

fun EditText.isEmpty() = getTextz().isEmpty()

fun isValidEmail(email: String): Boolean {
    val pattern = Regex("[a-zA-Z0-9._-]+@[a-z.]+\\.+[a-z]+")
    return pattern.matches(email)
}

fun Fragment.registerGreen() {
    org.greenrobot.eventbus.EventBus.getDefault().register(this)
}

fun Activity.registerGreen() {
    org.greenrobot.eventbus.EventBus.getDefault().register(this)
}

fun Fragment.unregisterGreen() {
    org.greenrobot.eventbus.EventBus.getDefault().unregister(this)
}

fun Activity.unregisterGreen() {
    org.greenrobot.eventbus.EventBus.getDefault().unregister(this)
}

fun Fragment.postNormal(event: Any) {
    org.greenrobot.eventbus.EventBus.getDefault().post(event)
}

// 6-2023 => 2023-6
fun String.convertMonthYear() : String{
    val month = this.substringBefore("-")
    val year = this.substringAfter("-")
    return "$year-$month"
}


fun handlerPostDelay(action: () -> Unit, delay: Long = 0L) {
    Looper.myLooper()?.let {
        Handler(it).postDelayed({
            action()
        }, delay)
    }
}
fun roundPercent(a: Int, b: Int): Double {
    return if (b != 0) {
        val result = (a.toDouble() / b.toDouble()) * 100
        val roundedResult = (result * 100).toLong() / 100.0
        roundedResult
    } else {
        0.00
    }
}

fun getTextValueChart(str: String): String {
    var result = str.trim()
    if (result.length > 13) {
        var int = 0
        if (result.contains(" ")) {
            "$result ".toCharArray().map {
                if (it == ' ') {
                    int += 1
                }
            }
        }
        result = if (int > 2 && result.indexOf(
                ' ',
                result.indexOf(' ', result.indexOf(' ') + 1) + 1
            ) > 0
        )
            result.substring(
                0,
                result.indexOf(' ', result.indexOf(' ', result.indexOf(' ') + 1) + 1)
            )
        else {
            str.substring(0, 13)
        }

        if (result.length < str.length)
            result = "$result..."
    }

    return result
}
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun formatMoney(
    value: Any,
    isAcceptZero: Boolean = true,
    isAcceptMinus: Boolean = true,
): String {
    var moneyFormat = "đ"
    try {
        val formatMoney: NumberFormat = NumberFormat.getCurrencyInstance(Locale.CANADA)
        val moneyFormatted: String = formatMoney.format(value)
        if (moneyFormatted.startsWith("-") && !isAcceptMinus) {
            if (isAcceptZero) {
                return "0 $moneyFormat"
            } else {
                return ""
            }
        } else {
            var valueFinal = moneyFormatted.replace("$", "")
            return if (valueFinal == "0") {
                if (isAcceptZero) {
                    "0 $moneyFormat"
                } else {
                    ""
                }
            } else {
                if (valueFinal.contains('.')) {
                    val position = valueFinal.lastIndexOf(".")
                    val checkZero = valueFinal.substring(position, valueFinal.length)
                    if (checkZero.trim().toDouble() == 0.0) {
                        valueFinal = valueFinal.removeRange(
                            position,
                            valueFinal.length
                        )
                    }
                }
                "$valueFinal $moneyFormat"
            }
        }
    } catch (ignore: Exception) {
        return "0 $moneyFormat"
    }
}

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
