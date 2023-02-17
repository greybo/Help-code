package com.example.help_code.presentation.pager

import android.os.Bundle
import android.view.View
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentPagerItem1Binding

class ItemPageFragment :
    BaseBindingFragment<FragmentPagerItem1Binding>(FragmentPagerItem1Binding::inflate) {

    //    private val args = arguments?.let { ItemPageFragmentArgs.fromBundle(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argsData = arguments?.getString("data")

        binding.pagerItemText.text = argsData ?: "Data"
    }
}