package com.example.help_code.presentation.pager

import android.os.Bundle
import android.view.View
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.PagerItem1FragmentBinding

class ItemPageFragment :
    BaseBindingFragment<PagerItem1FragmentBinding>(PagerItem1FragmentBinding::inflate) {

    //    private val args = arguments?.let { ItemPageFragmentArgs.fromBundle(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argsData = arguments?.getString("data")

        binding.pagerItemText.text = argsData ?: "Data"
    }
}