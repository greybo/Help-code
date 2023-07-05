package com.example.help_code.utilty

import android.os.SystemClock
import android.view.View
import timber.log.Timber


fun View.blockingClickListener(debounceTime: Long = 400L, action: (View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            val timeNow = SystemClock.elapsedRealtime()
            val elapsedTimeSinceLastClick = timeNow - lastClickTime
            if (elapsedTimeSinceLastClick < debounceTime) {
                Timber.d("Double click shielded")
                return
            } else {
                Timber.d("Click happened")
                action(v)
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.blockingClickListener(listener: View.OnClickListener?) {
    blockingClickListener {
        listener?.onClick(it)
    }
}