package com.example.help_code.utilty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T


inline fun <VB> Fragment.createBindingLazy(crossinline bindingInflater: Inflate<VB>): Lazy<VB> {
//    val classJava = VB::class.java
//    val clazz : Class<*> = Class.forName(classJava.name)
//    val ctor: Constructor<*> = clazz.getConstructor(classJava)
//    val instance = ctor.newInstance(/*arrayOf<Any>(args)*/)

    return lazy { bindingInflater.invoke(layoutInflater, null, false)!! }
}

fun <VB> Fragment.createBinding(bindingInflater: Inflate<VB>): VB {

    var _binding: VB? = bindingInflater.invoke(layoutInflater, null, false)

    if (context is DefaultLifecycleObserver) {
        DestroyBinding().block = { _binding = null }
    }

    return _binding!!
}

class DestroyBinding() : DefaultLifecycleObserver {
    var block: () -> Unit = {}
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        block()
    }

}


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