package com.example.help_code.presentation.module_test

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlank2Binding
import com.example.help_code.utilty.toastDebug


class BlankFragment : BaseBindingFragment<FragmentBlank2Binding>(FragmentBlank2Binding::inflate) {


    private val viewModel: BlankViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val current = resources.configuration.locale
        val country = current?.country
        toastDebug("country: $country")
    }
}