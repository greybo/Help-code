package au.com.crownresorts.crma.view.tooltip

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import au.com.crownresorts.crma.R

interface BaseTooltipCallback {
    fun initTooltip(callback: (() -> Unit)? = null): TooltipHandler?
}

fun Fragment.tooltipInit(callback: (() -> Unit)?): TooltipHandler? {
    return (requireContext() as? BaseTooltipCallback)?.initTooltip(callback)
}

fun Fragment.getTooltipHandler(
    message: String = "text",
    direction: ArrowDirection = ArrowDirection.TOP,
    wightPercent: Float = 0.8f,
    arrowOffset: Int = resources.getDimension(R.dimen.tooltip_edit_detail).toInt(),
    marginLeft: Int = resources.getDimension(R.dimen.tooltip_edit_detail).toInt(),
    dismissCallback: (() -> Unit)? = null
): TooltipHandler? {
    var tooltipView: ToolTipView? = requireActivity().findViewById(R.id.toolTipView)
    tooltipView ?: return null
    tooltipView.getBuilder()
        .addWightPercent(wightPercent)
        .addMessage(message)
        .addDirection(direction)
        .addDependLeftMargin(arrowOffset)
        .addDismissCallback(dismissCallback)
        .create()

    var observer: LifecycleCallbackObserver? = LifecycleCallbackObserver()

    val call: ((LifecycleOwner) -> Unit) = {
        tooltipView?.clean()
        tooltipView = null
        observer = null
    }

    observer?.invoke(call)

    if (requireContext() is LifecycleOwner) {
        observer?.let {
            (context as? LifecycleOwner)?.lifecycle?.addObserver(it)
        }
    }
    return tooltipView
}

class LifecycleCallbackObserver : DefaultLifecycleObserver {
    private var callback: ((LifecycleOwner) -> Unit)? = null

    operator fun invoke(callback: ((LifecycleOwner) -> Unit)?) {
        this.callback = callback
    }

    override fun onDestroy(owner: LifecycleOwner) {
        callback?.invoke(owner)
        owner.lifecycle.removeObserver(this)
        super.onDestroy(owner)
    }
}