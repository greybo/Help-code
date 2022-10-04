package com.example.help_code.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.example.help_code.start.MainRouter
import com.example.help_code.utilty.Inflate
import com.example.help_code.utilty.hideKeyboard

abstract class BaseBindingFragment<VB : ViewBinding>(private val bindingInflater: Inflate<VB>) :
    Fragment() {

//    abstract val route: MainRouter
//    abstract fun initToolbar(layout: ActionBarView)
//    val homeToolbarCallback = {
//        route.onBackPress()
//    }

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected open val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handlerDelayed = null
    }

    private var runnableDelay: Runnable? = null
    private var handlerDelayed: Handler? = Handler(Looper.getMainLooper())

    fun delay(callback: (Boolean) -> Unit) {
        if (runnableDelay != null) {
            handlerDelayed?.removeCallbacks(runnableDelay!!)
        }
        runnableDelay = Runnable {
            callback.invoke(true)
            runnableDelay = null
        }
        handlerDelayed?.postDelayed(runnableDelay!!, 500)
    }
}

fun Fragment.systemBackPressedCallback(callback: () -> Unit) {
    val context: Context = this.requireContext()
    val backPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                callback()
            }
        }
    this.requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    if (context is LifecycleOwner) {
        context.lifecycle.addObserver(CustomLifecycleObserver2(backPressedCallback))
    }
}

private class CustomLifecycleObserver2(val callback: OnBackPressedCallback) :
    DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
//        LogApp.i("CustomLifecycleObserver2: onDestroy")
        callback.remove()
        super.onDestroy(owner)
    }
}
