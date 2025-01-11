package au.com.crownresorts.crma.view.tooltip

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.help_code.R
import com.example.help_code.presentation.tooltip.my.ToolTipView
import com.example.help_code.presentation.tooltip.my.TooltipHandler


interface BaseTooltipCallback {
    fun initTooltip(callback: (() -> Unit)? = null): TooltipHandler?
}

fun Fragment.tooltipInit(callback: (() -> Unit)?): TooltipHandler? {
    return (requireContext() as? BaseTooltipCallback)?.initTooltip(callback)
}

fun Fragment.getTooltipHandler(
    message: String = "You cannot view this statement until you view your oldest unread statement",
    direction: ArrowDirection = ArrowDirection.TOP,
    wightPercent: Float = 0.8f,
    arrowOffset: Int = resources.getDimension(R.dimen.tooltip_edit_detail).toInt(),
    marginLeft: Int = resources.getDimension(R.dimen.tooltip_edit_detail).toInt(),
    dismissCallback: (() -> Unit)? = null
): TooltipHandler? {
    var tooltipView: ToolTipView? = ToolTipView(requireContext())
//    var tooltipView: ToolTipView? = requireActivity().findViewById(R.id.toolTipView)
    val viewGroup = (requireActivity().findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
    viewGroup.addView(tooltipView)
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