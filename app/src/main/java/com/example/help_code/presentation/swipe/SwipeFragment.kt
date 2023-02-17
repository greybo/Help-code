package com.example.help_code.presentation.swipe

import android.os.Bundle
import android.view.View
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentSwipeBinding


class SwipeFragment : BaseBindingFragment<FragmentSwipeBinding>(FragmentSwipeBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layout = binding.layoutTest//findViewById(R.id.layout_test) as RelativeLayout
        val test = binding.viewTest//findViewById(R.id.view_test) as TextView
        val swipe = Swipe(requireContext())
        swipe.addView(test)
        swipe.setLayout(layout)
    }

}