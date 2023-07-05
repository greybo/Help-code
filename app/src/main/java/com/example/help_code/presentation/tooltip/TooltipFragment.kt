package com.example.help_code.presentation.tooltip

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.help_code.R
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentSwipeBinding
import com.example.help_code.databinding.FragmentTooltipBinding
import com.skydoves.balloon.*


class TooltipFragment : BaseBindingFragment<FragmentTooltipBinding>(FragmentTooltipBinding::inflate) {

//    private val profileBalloon by balloon<ProfileBalloonFactory>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val balloon = Balloon.Builder(requireContext())
            .setWidthRatio(1.0f)
            .setText("You cannot view this statement until you view your oldest unread statement")
            .setTextColorResource(R.color.white)
            .setTextSize(15f)
            .setTextGravity(Gravity.START)
//            .setIconDrawableResource(R.drawable.ic_copy)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowOrientation(ArrowOrientation.BOTTOM)
            .setArrowSize(10)
            .setArrowPosition(0.5f)
            .setWidthRatio(0.6f)
            .setPadding(12)
            .setCornerRadius(8f)
            .setBackgroundColorResource(R.color.gray)
            .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
            .setLifecycleOwner(viewLifecycleOwner)

//            .setWidth(20)
//            .setHeight(20)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)

            .build()


        binding.tooltipAnchorIcon.setOnClickListener {
            balloon.showAlignTop(it)
        }
        binding.tooltipAnchorText.setOnClickListener {
            balloon.showAlignBottom(it)
        }
    }

}