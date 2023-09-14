package com.example.help_code.presentation.tooltip


import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.help_code.R
import com.example.help_code.databinding.LayoutCustomProfileBinding
import com.example.help_code.utilty.gone
import com.example.help_code.utilty.visible
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayCircle
import timber.log.Timber


fun Fragment.getTooltipBalloonCustom(
    message: String,
    iconRes: Int? = null,//= R.drawable.ic_across_close,
    dismissCallback: (() -> Unit)? = null
): Balloon {
    val binding = LayoutCustomProfileBinding.inflate(layoutInflater, null, false)

    binding.toolTipMessageText.text = message

    val balloon = createBalloon(requireContext()) {
        setLayout(binding.root)

//        .setArrowOrientation(ArrowOrientation.BOTTOM)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setArrowSize(10)
        setArrowPosition(0.5f)

        setBackgroundColorResource(R.color.grey)
        setCornerRadius(8f)
        setBalloonAnimation(BalloonAnimation.OVERSHOOT)
        setLifecycleOwner(viewLifecycleOwner)

        setMarginRight(8)

//        .setPadding(10)

        setWidthRatio(0.7f)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)

        setOnBalloonOverlayClickListener(OnBalloonOverlayClickListener { Timber.d("setOnBalloonOverlayClickListener") })
        // Overlay
        setIsVisibleOverlay(true) // sets the visibility of the overlay for highlighting an anchor.
        setOverlayColorResource(R.color.transparency_50) // background color of the overlay using a color resource.
        setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE) // default is fade.
        setOverlayShape(BalloonOverlayCircle(radius = 0f))
        setDismissWhenOverlayClicked(false) // disable dismissing the balloon when the overlay is clicked.
        dismissWhenClicked = false
        setOnBalloonDismissListener {
            dismissCallback?.invoke()
        }
    }

    binding.toolTipCloseButton.setIconOrGone(iconRes) {
        it.setOnClickListener {
            balloon.dismiss()
        }
    }

    return balloon
}

private fun ImageView.setIconOrGone(iconRes: Int?, function: (ImageView) -> Unit) {
    iconRes?.let {
        this.visible()
        setImageResource(it)
        function(this)
    } ?: this.gone()

}

