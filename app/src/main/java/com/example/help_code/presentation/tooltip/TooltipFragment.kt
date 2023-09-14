package com.example.help_code.presentation.tooltip

import android.os.Bundle
import android.view.View
import com.example.help_code.R
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentTooltipBinding
import com.skydoves.balloon.balloon
import timber.log.Timber


class TooltipFragment : BaseBindingFragment<FragmentTooltipBinding>(FragmentTooltipBinding::inflate) {

    private val profileBalloon by balloon<ProfileBalloonFactory>()
    private val messageText = "You cannot view this statement until you view your oldest unread statement"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val myTooltip = getTooltipHandler()
//        binding.tooltipAnchorIcon2.setOnClickListener {
//            myTooltip?.show(it)
//        }

        val balloon1 = getTooltipBalloon(messageText)
        val balloon2 = profileBalloon
        val balloon3 = getTooltipBalloonCustom(messageText, R.drawable.ic_across_close)
        val balloon4 = getTooltipBalloonCustom("4  $messageText") {
            Timber.d("getTooltipBalloonCustom 4")
        }

        binding.tooltipAnchorText1.setOnClickListener {
            balloon1.showAlignBottom(it)
        }
        binding.tooltipAnchorIcon2.setOnClickListener {
            balloon2.showAlignTop(it)
        }

        binding.tooltipAnchorIcon3.setOnClickListener {
            balloon3.showAlignBottom(it)
        }
        binding.tooltipAnchorIcon4.setOnClickListener {
            balloon4.showAlignBottom(it)
        }
    }

}