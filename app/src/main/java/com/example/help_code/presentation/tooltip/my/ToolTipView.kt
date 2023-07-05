package com.example.help_code.presentation.tooltip.my

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import au.com.crownresorts.crma.view.tooltip.ArrowDirection
import com.example.help_code.R
import com.example.help_code.databinding.ToolTipLayoutBinding
import com.example.help_code.utilty.*
import timber.log.Timber

interface TooltipHandler {
    fun show(anchor: View)
    fun dismiss()
    fun clean()
}

class ToolTipView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : FrameLayout(context, attrs, defStyleAttr), TooltipHandler {

    private var toolTipLayout: LinearLayout? = null
    private var arrowView: View? = null
    private var closeButton: View? = null
    private var messageTextView: TextView? = null
    private var toolTipArrowView: View? = null
    private var _binding: ToolTipLayoutBinding?
    private var buttonClose: (() -> Unit)? = null

    private var dependLeftMargin: Int = 0
    private var message: String = ""
    private var isShow: Boolean = false
    private var relativeToSelfX: Float = 0f
    private var biasX: Int = 0
    private val widthScreen: Int by lazy { getScreenWidth(context) }
    private var paramsTextLayout: LinearLayout.LayoutParams? = null
    private var paramsArrow: LinearLayout.LayoutParams? = null
    private var paramsLayout: LayoutParams? = null
    private var defaultDirection: ArrowDirection = ArrowDirection.BOTTOM
    private var direction: ArrowDirection = ArrowDirection.BOTTOM
    private var wightPercent: Float = 0f
    private val offset: Int by lazy { resources.getDimension(R.dimen.tooltip_offset_4).toInt() }
    private val rectDepend = Rect()
    private var pivotYValue: Float = 0f

    private val TAG = "ToolTipView"

    init {
        val view = View.inflate(context, R.layout.tool_tip_layout, this)
        _binding = ToolTipLayoutBinding.bind(view)
        this.invisible()
    }

    private fun init(direction: ArrowDirection) {
        val binding = _binding ?: return
        when (direction) {
            ArrowDirection.BOTTOM -> {
                arrowView = binding.bottomLayout.toolTipArrowView
                toolTipLayout = binding.bottomLayout.toolTipLayout
                closeButton = binding.bottomLayout.toolTipCloseButton
                messageTextView = binding.bottomLayout.toolTipMessageText
                toolTipArrowView = binding.bottomLayout.toolTipArrowView
            }
            ArrowDirection.TOP -> {
                arrowView = binding.topLayout.toolTipArrowView
                toolTipLayout = binding.topLayout.toolTipLayout
                closeButton = binding.topLayout.toolTipCloseButton
                messageTextView = binding.topLayout.toolTipMessageText
                toolTipArrowView = binding.topLayout.toolTipArrowView
            }
        }
        messageTextView?.text = this.message
        closeButton?.blockingClickListener {
            clearView()
        }
        this.blockingClickListener {
            clearView()
        }
        arrowView?.background = ArrowDrawable(context.getColorSafe(R.color.gray), direction.ordinal)
        this.invisible()
    }

    fun setBuildModel(buildModel: TooltipBuildModel): TooltipHandler {
        Timber.i(TAG, "---------------------------------------")
        this.wightPercent = buildModel.wightPercent
        this.message = buildModel.message
        this.dependLeftMargin = buildModel.dependLeftMargin
        this.defaultDirection = buildModel.direction
        this.buttonClose = buildModel.dismissCallback

        return this
    }

    override fun show(anchor: View) {
        _binding ?: return
        anchor.getGlobalVisibleRect(rectDepend)
        createTooltip(defaultDirection)
    }

    override fun dismiss() {
        hideTooltip()
    }

    override fun clean() {
        toolTipLayout = null
        arrowView = null
        closeButton = null
        messageTextView = null
        toolTipArrowView = null
        _binding = null
        buttonClose = null
    }

    private fun createTooltip(direction: ArrowDirection) {
        this.direction = direction
        pivotYValue = when (direction) {
            ArrowDirection.BOTTOM -> 1f
            ArrowDirection.TOP -> 0f
        }
        init(direction)

        val marginTooltipLayout = ((widthScreen * (1 - wightPercent)) / 2).toInt()
        (toolTipLayout?.layoutParams as? LayoutParams)?.setMargins(marginTooltipLayout, 0, marginTooltipLayout, 0)

        moveArrowHorizontal(this.dependLeftMargin)
        moveLayoutVertical()
    }

    private fun moveArrowHorizontal(dependLeftMargin: Int) {
        if (paramsArrow == null) {
            val centreDependView = (rectDepend.right - rectDepend.left) / 2

            relativeToSelfX = (rectDepend.left) * (1 / (widthScreen.toFloat() - (dependLeftMargin * 2)))
            Timber.i(TAG, "moveArrowHorizontal: dependLeftMargin=${dependLeftMargin}")
            Timber.i(TAG, "moveArrowHorizontal: centreDependView=${centreDependView}")
            Timber.i(TAG, "moveArrowHorizontal: widthScreen=${widthScreen}")
            Timber.i(TAG, "moveArrowHorizontal: relativeToSelfX=$relativeToSelfX")
            Timber.i(TAG, "moveArrowHorizontal: rectDepend=$rectDepend")
            Timber.i(TAG, "moveArrowHorizontal: offset=$offset")

            val margeOffset = dependLeftMargin + centreDependView - offset
            biasX = (rectDepend.left - margeOffset)
            paramsArrow = arrowView?.layoutParams as LinearLayout.LayoutParams
            paramsArrow?.marginStart = biasX
            arrowView?.layoutParams = paramsArrow
        }
    }

    private fun moveLayoutVertical() {
        Timber.i(TAG, "moveLayoutVertical init listener")
        toolTipLayout?.viewTreeObserver?.addOnGlobalLayoutListener(toolTipLayoutListener)
    }

    private val toolTipLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        Timber.i(TAG, "moveLayoutVertical viewTreeObserver")
        if (paramsLayout == null) {
            val rectTooltipLayout = Rect()
            val heightStatusBar = getStatusBarSize()
            toolTipLayout?.getGlobalVisibleRect(rectTooltipLayout)
            val heightBox = rectTooltipLayout.bottom - rectTooltipLayout.top

            val moveY = when (direction) {
                ArrowDirection.BOTTOM -> {
                    rectDepend.top - heightBox - heightStatusBar
                }
                ArrowDirection.TOP -> {
                    rectDepend.bottom - heightStatusBar
                }
            }
            Timber.i(TAG, "moveLayoutVertical: ${rectTooltipLayout}, height: $heightBox, heightStatusBar: $heightStatusBar")
            paramsLayout = toolTipLayout?.layoutParams as LayoutParams
            paramsLayout?.topMargin = moveY
            toolTipLayout?.layoutParams = paramsLayout
            checkSizeTooltip(moveY)
        }
    }

    private fun checkSizeTooltip(moveY: Int) {
        if (moveY < 0) {
            val direction = when (direction) {
                ArrowDirection.BOTTOM -> ArrowDirection.TOP
                ArrowDirection.TOP -> ArrowDirection.BOTTOM
            }
            clearView()
            createTooltip(direction)
        } else {
            showTooltip()
        }
    }

    private fun clearView() {
        hideTooltip()
        toolTipLayout?.viewTreeObserver?.removeOnGlobalLayoutListener(toolTipLayoutListener)
        paramsLayout = null
        paramsArrow = null
        paramsTextLayout = null
        isShow = false
    }

    private fun showTooltip() {
        if (paramsLayout != null && paramsArrow != null && !isShow) {
            isShow = true
            this.visible()
            scaleView(toolTipLayout!!, 0f, 1f, relativeToSelfX)
        }
    }

    private fun hideTooltip(quiet: Boolean = false) {
        if (isShow) {
            buttonClose?.invoke()
            if (quiet) {
                clearView()
                this.invisible()
            } else {
                scaleView(toolTipLayout!!, 1f, 0f, relativeToSelfX) {
                    clearView()
                    this.invisible()
                }
            }
        }
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    private fun getStatusBarSize(): Int {
        val resource = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resource > 0) {
            context.resources.getDimensionPixelSize(resource)
        } else 0
    }

    private fun scaleView(v: View, startScale: Float, endScale: Float, relativeToSelfX: Float, function: (() -> Unit)? = null) {
        val anim: Animation = ScaleAnimation(
            startScale, endScale,  // Start and end values for the X axis scaling
            startScale, endScale,  // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, relativeToSelfX,  // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, pivotYValue//
        ) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 200
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                function?.invoke()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        v.startAnimation(anim)
    }

    fun getBuilder(): Build {
        return Build()
    }

    inner class Build {
        private val buildModel = TooltipBuildModel()

        /**
         *  set default value 0.85
         *  if need to set a custom value
         */
        fun addWightPercent(wightPercent: Float): Build {
            buildModel.wightPercent = wightPercent
            return this
        }

        /**
         *  set default value TOP
         *  if need to set a custom value
         */
        fun addDirectionTop(): Build {
            buildModel.direction = ArrowDirection.TOP
            return this
        }

        /**
         *  set default value TOP
         *  if need to set a custom value
         */
        fun addDirectionBottom(): Build {
            buildModel.direction = ArrowDirection.BOTTOM
            return this
        }

        fun addDirection(direction: ArrowDirection): Build {
            buildModel.direction = direction
            return this
        }

        /**
         *  need to set the left margin value
         */
        fun addDependLeftMargin(dependLeftMargin: Int): Build {
            buildModel.dependLeftMargin = dependLeftMargin
            return this
        }

        /**
         *  set default value ContentKey.AmlIntroTooltipText.get()
         *  if need to set a custom value
         */
        fun addMessage(message: String): Build {
            buildModel.message = message
            return this
        }

        fun addDismissCallback(callback: (() -> Unit)?): Build {
            buildModel.dismissCallback = callback
            return this
        }

        fun create(): TooltipHandler {
            return setBuildModel(buildModel)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val r1 = Rect()
        toolTipLayout?.getGlobalVisibleRect(r1)
        val containsTouch = r1.contains(event.x.toInt(), event.y.toInt())
        if (!containsTouch) clearView()
        return true
    }
}

data class TooltipBuildModel(
    var wightPercent: Float = 0.85f,
    var direction: ArrowDirection = ArrowDirection.TOP,
    var dependLeftMargin: Int = 0,
    var message: String = "Text",
    var dismissCallback: (() -> Unit)? = null
)