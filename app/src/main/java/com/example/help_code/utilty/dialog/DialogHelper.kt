package com.example.help_code.utilty.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.help_code.R
import com.example.help_code.utilty.getButtonName


fun presentNetworkErrorAlert(context: Context?) {
    android.app.AlertDialog.Builder(context)
        .setTitle("Network unavailable")
        .setMessage("Please check your internet connection and try again.")
        .setCancelable(true)
        .setPositiveButton("OK", null)
        .show()
}

data class GlobalDialogData(val title: String?, val message: String?, val positive: String?, val negative: String?)

fun Fragment.dialogGlobalWithCallback(
    title: String? = null,
    message: String? = null,
    positive: String? = null,
    negative: String? = null,
    action: ((Boolean, String) -> Unit)? = null
) {
    dialogGlobalWithCallback(GlobalDialogData(title, message, positive, negative), action)
}

fun Fragment.dialogGlobalWithCallback(data: GlobalDialogData, action: ((Boolean, String) -> Unit)? = null) {
    val dialogBuild = FullCustomDialog.Builder()
    if (!data.title.isNullOrEmpty()) {
        dialogBuild
            .title(data.title)
    }
    if (!data.message.isNullOrEmpty()) {
        dialogBuild.message(data.message)
    }
    if (!data.positive.isNullOrEmpty()) {
        dialogBuild.positive(data.positive)
            .positiveCallback {
                action?.invoke(true, it.getButtonName())
            }
    }
    if (!data.negative.isNullOrEmpty()) {
        dialogBuild.negative(data.negative)
            .negativeCallback {
                action?.invoke(false, it.getButtonName())
            }
    }

    dialogBuild.build()
        .show(requireContext())
}

fun showFullScreenDialog(context: Context): Dialog {
    val dialog = AlertDialog.Builder(context, R.style.WhiteDialog).create().apply {
        window?.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        setCancelable(false)
    }
    if (context is LifecycleOwner) {
        context.lifecycle.addObserver(DialogLifecycleObserver(dialog))
    }
    return dialog
}


//fun Fragment.globalCloseDialogWithAnalytics(
//    helper: IAnalyticsHelperBase,
//    dialogScreenName: IScreenName,
//    pathEnd: String? = null,
//    callback: (Boolean) -> Unit
//) {
//    val context = requireContext()
//    val info = AnalyticsInfo(pathEnd = pathEnd)
//    helper.trackScreenPopUp(dialogScreenName, info)
//    closeDialog(
//        context,
//        "${ContentKey.CommonDialogCloseHeading.get()} ${ContentKey.CommonDialogCloseText.get()}"
//    ) { isYes, name ->
//        info.itemName = name
//        helper.trackContentPopUp(info)
//        callback(isYes ?: false)
//    }
//}
//
//fun closeDialog(
//    context: Context?,
//    text: String? = null,
//    positiveButtonText: String? = null,
//    negativeButtonText: String? = null,
//    callback: ((Boolean?, String) -> Unit)?
//) {
//    if (context == null) return
//    val binding = bindingDialog(context, DialogCancelBinding::inflate)
//
//    with(binding) {
//        dialogMessage.text = if (text.isNullOrEmpty()) "${ContentKey.CommonDialogCloseHeading.get()} ${ContentKey.CommonDialogCloseText.get()}" else text
//        dialogButtonPositive.text = if (positiveButtonText.isNullOrEmpty()) context.getString(R.string.close_dialog_name_close) else positiveButtonText
//        dialogButtonNegative.text = if (negativeButtonText.isNullOrEmpty()) context.getString(R.string.close_dialog_name_cancel) else negativeButtonText
//    }
//    val dialog = AlertDialog.Builder(context)
//        .setCancelable(false)
//        .setView(binding.root)
//        .show()
//    binding.dialogButtonPositive.blockingClickListener {
//        dialog?.dismiss()
//        callback?.invoke(true, it.getButtonName())
//    }
//    binding.dialogButtonNegative.blockingClickListener {
//        dialog?.dismiss()
//        callback?.invoke(false, it.getButtonName())
//    }
//    if (context is LifecycleOwner) {
//        context.lifecycle.addObserver(DialogLifecycleObserver(dialog))
//    }
//}
//
//
//fun cancelDialog(context: Context?, text: String? = null, callback: ((Boolean?, String) -> Unit)?): AlertDialog? {
//    if (context == null || text == null) return null
//    val binding = bindingDialog(context, DialogCancelBinding::inflate)
//
//    with(binding) {
//        dialogMessage.text = text
//        dialogButtonPositive.text = context.getString(R.string.pin_reset_dialog_yes)
//        dialogButtonNegative.text = context.getString(R.string.pin_reset_dialog_cansel)
//    }
//    val dialog = AlertDialog.Builder(context)
//        .setView(binding.root)
//        .setCancelable(false)
//        .show()
//    binding.dialogButtonPositive.blockingClickListener {
//        dialog.dismiss()
//        callback?.invoke(true, it.getButtonName())
//    }
//    binding.dialogButtonNegative.blockingClickListener {
//        dialog.dismiss()
//        callback?.invoke(false, it.getButtonName())
//    }
//
//    if (context is LifecycleOwner) {
//        context.lifecycle.addObserver(DialogLifecycleObserver(dialog))
//    }
//    return dialog
//}
//
//
//
//fun Fragment.pdfDownloadErrorDialog(callback: (Boolean, String?) -> Unit = { _, _ -> }) {
//    newErrorDialog(
//        title = ContentKey.DetailsPasListErrorHeader.get(),
//        message = ContentKey.DetailsPasListErrorText.get(),
//        positive = ContentKey.DetailsPasListErrorRetry.get(),
//        negative = ContentKey.DetailsPasListErrorGoBack.get(),
//        callback = callback
//    )
//}
//
//fun Fragment.newErrorDialog(
//    context: Context? = requireContext(),
//    title: String? = null,
//    message: String? = null,
//    positive: String? = null,
//    negative: String? = null,
//    callback: (Boolean, String?) -> Unit
//) {
//    if (context == null) {
//        return
//    }
//    val binding = bindingDialog(context, DialogDownloadErrorBinding::inflate)
//    val mAlertDialog = AlertDialog.Builder(context)
//        .setView(binding.root)
//        .show()
//
//    with(binding) {
//        errorDialogHeading.text = title ?: context.getString(R.string.dialog_no_longer_available_title)
//        errorDialogMessage.text = message ?: context.getString(R.string.dialog_no_longer_available_message)
//
//        errorDialogPositive.apply {
//            setTextOrGone(positive)
//            blockingClickListener {
//                mAlertDialog?.dismiss()
//                callback.invoke(true, positive)
//            }
//        }
//
//        errorDialogNegative.apply {
//            setTextOrGone(positive)
//            blockingClickListener {
//                mAlertDialog?.dismiss()
//                callback.invoke(false, negative)
//            }
//        }
//    }
//
//    mAlertDialog.setOnCancelListener {
//        callback.invoke(false, "cancel")
//    }
//    if (context is LifecycleOwner) {
//        context.lifecycle.addObserver(DialogLifecycleObserver(mAlertDialog))
//    }
//}