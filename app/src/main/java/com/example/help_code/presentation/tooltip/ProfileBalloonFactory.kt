package com.example.help_code.presentation.tooltip

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.help_code.R
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.createBalloon

class ProfileBalloonFactory : Balloon.Factory() {

  override fun create(context: Context, lifecycle: LifecycleOwner?): Balloon {

      return createBalloon(context) {
      setLayout(R.layout.layout_custom_tooltip)
      setArrowSize(10)
      setArrowOrientation(ArrowOrientation.TOP)
      setArrowPosition(0.5f)
      setWidthRatio(0.55f)
//      setHeight(250)
      setCornerRadius(4f)
      setBackgroundColor(ContextCompat.getColor(context, R.color.gray))
      setBalloonAnimation(BalloonAnimation.CIRCULAR)
      setLifecycleOwner(lifecycle)
    }
  }


}