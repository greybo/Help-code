package com.example.help_code.utilty.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class CustomDialogFragment private constructor(builder: DialogFragmentModel?) : DialogFragment() {

    private val title: String? = builder?.title
    private val message: String? = builder?.text
    private val positiveName: String? = builder?.positiveName
    private val negativeName: String? = builder?.negativeName
    private var callback: ((String, Boolean) -> Unit)? = builder?.callback

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity: Activity? = activity
        return with(android.app.AlertDialog.Builder(activity)) {
            title?.let { setTitle(it) }
            message?.let { setMessage(it) }
            positiveName?.let {
                this.setPositiveButton(it) { _, _ ->
                    callback?.invoke(it, true)
                }
            }
            negativeName?.let {
                this.setNegativeButton(it) { _, _ ->
                    callback?.invoke(it, false)
                }
            }
            this.create()
        }
    }

    companion object {
        fun setBuilder(model: DialogFragmentModel?): CustomDialogFragment {
            return CustomDialogFragment(model)
        }
    }

    override fun onDestroy() {
        callback = null
        super.onDestroy()
    }
}

class DialogFragmentBuilder {
    // builder code
    private var title: String? = null
    private var message: String? = null
    private var positive: String? = null
    private var negative: String? = null
    private var callback: ((String, Boolean) -> Unit)? = null

    fun title(title: String?) = apply { this.title = title }
    fun message(message: String?) = apply { this.message = message }
    fun positive(positive: String?) = apply { this.positive = positive }
    fun negative(negative: String?) = apply { this.negative = negative }
    fun onClickListener(callback: ((String, Boolean) -> Unit)?) = apply { this.callback = callback }

    fun create() = CustomDialogFragment.setBuilder(DialogFragmentModel(title, message, negative, positive, callback))
}

data class DialogFragmentModel(
    val title: String?,
    val text: String?,
    val negativeName: String?,
    val positiveName: String?,
    val callback: ((String, Boolean) -> Unit)?
)