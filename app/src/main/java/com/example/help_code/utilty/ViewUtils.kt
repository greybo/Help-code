package com.example.help_code.utilty


import android.content.Context
import android.content.res.Resources
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.google.android.material.textfield.TextInputLayout

/**
 * Extension for make any View visible in screen
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Extension for make any View gone in screen
 */
fun View.gone() {
    visibility = View.GONE
}

fun View.setGoneOrVisible(state: Boolean) {
    if (state) {
        visible()
    } else {
        gone()
    }
}

fun View.setIsVisible(state: Boolean) {
    if (state) {
        visible()
    } else {
        invisible()
    }
}

/**
 * Extension for make any View invisible in screen
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.active() {
    this.alpha = 1.0f
    this.isActivated = true
    this.isEnabled = true
}

fun View.inactive() {
    this.alpha = 0.5f
    this.isActivated = false
    this.isEnabled = false
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun androidx.fragment.app.Fragment.toast(message: String) {
    if (context != null)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun TextInputLayout.simpleError() {
    /**
     * Hot fix to prevent show white space under TextInputLayout
     */
    error = " "
    if (childCount == 2) {
        getChildAt(1).visibility = View.GONE
    }
}

fun View.setTextOrGone(text: String?, callback: ((Boolean) -> Unit)? = null) {
    val isVisible = !text.isNullOrEmpty()
    this.setGoneOrVisible(isVisible)
    if (isVisible) {
        when (this) {
            is TextView -> this.text = text
            is Button -> this.text = text
            is CheckBox -> this.text = text
        }
    }
    callback?.invoke(isVisible)

}

fun View.setTextOrGoneByResId(idString: Int?) {
    val text = idString?.let { this.context.getString(it) }
    if (!TextUtils.isEmpty(text)) {
        this.visible()
        when (this) {
            is TextView -> this.text = text
            is Button -> this.text = text
        }
    } else this.gone()
}

val DP_IN_PX = Resources.getSystem().displayMetrics.density
val SP_IN_PX = Resources.getSystem().displayMetrics.scaledDensity
