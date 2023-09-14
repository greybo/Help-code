package com.example.help_code.presentation.tooltip

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.help_code.R
import com.skydoves.balloon.*

class ProfileBalloonFactory : Balloon.Factory() {

    override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {

        return createBalloon(context) {
            setLayout(R.layout.layout_custom_tooltip)
            setArrowSize(10)
            setArrowOrientation(ArrowOrientation.TOP)
            setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            setArrowPosition(0.5f)
            setWidthRatio(0.7f)
            setPadding(10)
//      setHeight(250)
            setCornerRadius(4f)
            setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
            setBalloonAnimation(BalloonAnimation.CIRCULAR)
            setLifecycleOwner(lifecycle)
        }
    }


}