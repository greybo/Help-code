package com.example.help_code.utilty.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.example.help_code.databinding.DialogTitleTextYesNoBinding
import com.example.help_code.databinding.DialogTitleTextYesNoVerticalBinding
import com.example.help_code.utilty.bindingDialog
import com.example.help_code.utilty.blockingClickListener

class FullCustomDialog private constructor(builder: Builder) {
    private val title: String?
    private val message: String?
    private val positive: String?
    private val negative: String?
    private val positiveCallback: ((View) -> Unit)?
    private val negativeCallback: ((View) -> Unit)?
    private val twoLine: Boolean

    init {
        this.title = builder.title
        this.message = builder.message
        this.positive = builder.positive
        this.negative = builder.negative
        this.positiveCallback = builder.positiveCallback
        this.negativeCallback = builder.negativeCallback
        this.twoLine = builder.vertical
    }

    fun show(context: Context) {
        if (twoLine) show2(context)
        else show1(context)
    }

    private fun show1(context: Context) {
        val binding = bindingDialog(context, DialogTitleTextYesNoBinding::inflate)
        with(binding) {

            if (title.isNullOrEmpty()) {
                dialogTitle.visibility = View.GONE
            } else {
                dialogTitle.visibility = View.VISIBLE
                dialogTitle.text = title //"Scan ID again"
            }

            if (message.isNullOrEmpty()) {
                dialogMessage.visibility = View.GONE
            } else {
                dialogMessage.visibility = View.VISIBLE
                dialogMessage.text = message
            }

            if (positive.isNullOrEmpty()) {
                dialogButtonPositive.visibility = View.GONE
            } else {
                dialogButtonPositive.visibility = View.VISIBLE
                dialogButtonPositive.text = positive
            }

            if (negative.isNullOrEmpty()) {
                dialogButtonNegative.visibility = View.GONE
            } else {
                dialogButtonNegative.visibility = View.VISIBLE
                dialogButtonNegative.text = negative
            }
        }

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .show()

        binding.dialogButtonPositive.blockingClickListener {
            dialog?.dismiss()
            positiveCallback?.invoke(it)
        }

        binding.dialogButtonNegative.blockingClickListener {
            dialog?.dismiss()
            negativeCallback?.invoke(it)
        }

        (context as? LifecycleOwner)?.lifecycle?.addObserver(DialogLifecycleObserver(dialog))
    }

    private fun show2(context: Context) {
        val binding = bindingDialog(context, DialogTitleTextYesNoVerticalBinding::inflate)
        with(binding) {

            if (title.isNullOrEmpty()) {
                dialogTitle.visibility = View.GONE
            } else {
                dialogTitle.visibility = View.VISIBLE
                dialogTitle.text = title //"Scan ID again"
            }

            if (message.isNullOrEmpty()) {
                dialogMessage.visibility = View.GONE
            } else {
                dialogMessage.visibility = View.VISIBLE
                dialogMessage.text = message
            }

            if (positive.isNullOrEmpty()) {
                dialogButtonPositive.visibility = View.GONE
            } else {
                dialogButtonPositive.visibility = View.VISIBLE
                dialogButtonPositive.text = positive
            }

            if (negative.isNullOrEmpty()) {
                dialogButtonNegative.visibility = View.GONE
            } else {
                dialogButtonNegative.visibility = View.VISIBLE
                dialogButtonNegative.text = negative
            }
        }

        val dialog = AlertDialog.Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .show()

        binding.dialogButtonPositive.blockingClickListener {
            dialog?.dismiss()
            positiveCallback?.invoke(it)
        }

        binding.dialogButtonNegative.blockingClickListener {
            dialog?.dismiss()
            negativeCallback?.invoke(it)
        }

        (context as? LifecycleOwner)?.lifecycle?.addObserver(DialogLifecycleObserver(dialog))
    }

    class Builder {
        // builder code
        var title: String? = null
            private set
        var message: String? = null
            private set
        var positive: String? = null
            private set
        var negative: String? = null
            private set
        var positiveCallback: ((View) -> Unit)? = null
            private set
        var negativeCallback: ((View) -> Unit)? = null
            private set
        var vertical: Boolean = false
            private set

        fun title(title: String?) = apply { this.title = title }
        fun message(message: String?) = apply { this.message = message }
        fun positive(positive: String?) = apply { this.positive = positive }
        fun negative(negative: String?) = apply { this.negative = negative }
        fun positiveCallback(callback: ((View) -> Unit)?) = apply { this.positiveCallback = callback }
        fun negativeCallback(callback: ((View) -> Unit)?) = apply { this.negativeCallback = callback }
        fun verticalButtons() = apply { this.vertical = true }
        fun build() = FullCustomDialog(this)
    }
}