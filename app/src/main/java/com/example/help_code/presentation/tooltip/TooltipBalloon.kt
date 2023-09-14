package com.example.help_code.presentation.tooltip


import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.help_code.R
import com.skydoves.balloon.*
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayCircle
import timber.log.Timber


fun Fragment.getTooltipBalloon(message: String, iconRes: Int? = null, dismiss: (() -> Unit)? = null): Balloon {

    return createBalloon(requireContext()) {

        setTextForm(createTextForm(message))
        iconRes?.let {
            setIconForm(createIconForm(iconRes))
        }
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setArrowOrientation(ArrowOrientation.BOTTOM)
        setArrowSize(10)
        setArrowPosition(0.5f)

        setBackgroundColorResource(R.color.colorPrimary)
        setCornerRadius(8f)
        setBalloonAnimation(BalloonAnimation.OVERSHOOT)
        setLifecycleOwner(viewLifecycleOwner)

        setPaddingRight(10)
        setPaddingLeft(12)
        setPaddingBottom(10)
        setPaddingTop(10)
        setWidthRatio(0.6f)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setMarginRight(6)
        // Overlay
        setIsVisibleOverlay(true) // sets the visibility of the overlay for highlighting an anchor.
        setOverlayColorResource(R.color.transparency_50) // background color of the overlay using a color resource.
        setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE) // default is fade.
        setDismissWhenOverlayClicked(false) // disable dismissing the balloon when the overlay is clicked.
        setOverlayPaddingColorResource(R.color.transparency_50) // sets color of the overlay padding using a color resource.
        setOverlayShape(BalloonOverlayCircle(radius = 0f))

        onBalloonDismissListener = OnBalloonDismissListener {
            dismiss?.invoke()
            Timber.i("balloon Dismiss to close")
        }
    }
}

fun Fragment.createIconForm(iconRes: Int): IconForm {
    return iconForm(requireContext()) {
        setDrawable(ContextCompat.getDrawable(requireContext(), iconRes))
//        .setIconColor(ContextCompat.getColor(requireContext(), R.color.skyblue))
        setIconSize(30)
        setDrawableGravity(IconGravity.END)
    }

}

fun Fragment.createTextForm(message: String): TextForm {
    return textForm(requireContext()) {
        setText(message)
        setTextColorResource(R.color.text_color_inverted)
        setTextSize(13f)
        setTextGravity(Gravity.START)
        setTextLineSpacing(1f)
    }
}