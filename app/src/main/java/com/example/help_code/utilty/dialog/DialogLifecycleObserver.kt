package com.example.help_code.utilty.dialog

import android.app.Dialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class DialogLifecycleObserver(private val dialog: Dialog?) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        dialog?.dismiss()
        super.onDestroy(owner)
    }
}
