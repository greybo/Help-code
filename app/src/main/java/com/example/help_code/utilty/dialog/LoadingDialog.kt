package com.example.help_code.utilty.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.example.help_code.R

class LoadingDialog(context: Context, val text: String) : Dialog(context) {

    init {
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(DialogLifecycleObserver(this))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(R.layout.dialog_loading)
        findViewById<TextView>(R.id.loadingDialogTextView).text = text
    }
}