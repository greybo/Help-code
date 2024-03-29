package com.example.help_code.utilty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

fun <VB> bindingDialog(context: Context, layoutInflater: Inflate<VB>): VB {
    return layoutInflater.invoke(LayoutInflater.from(context), null, false)
}

fun <VB> ViewGroup.inflateCustomView(context: Context, layoutInflater: Inflate<VB>): VB {
    return layoutInflater.invoke(LayoutInflater.from(context), this, true)
}

fun <VB> ViewGroup.inflateAdapter(layoutInflater: Inflate<VB>): VB {
    return layoutInflater.invoke(LayoutInflater.from(this.context), this, false)
}

fun ViewGroup.inflate(@LayoutRes layout: Int): View {
    return LayoutInflater.from(context).inflate(layout, this, false)
}


fun <VB> bindingDialog(layoutInflater: LayoutInflater, viewInflater: Inflate<VB>): VB {
    return viewInflater.invoke(layoutInflater, null, false)
}

fun <VB> ViewGroup.bindingCustomView(context: Context, layoutInflater: Inflate<VB>): VB {
    return layoutInflater.invoke(LayoutInflater.from(context), this, true)
}