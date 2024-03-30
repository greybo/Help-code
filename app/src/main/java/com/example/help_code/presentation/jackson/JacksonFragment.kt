package com.example.help_code.presentation.jackson

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentJacksonTestBinding

class JacksonFragment :
    BaseBindingFragment<FragmentJacksonTestBinding>(FragmentJacksonTestBinding::inflate) {
    private val viewModel: JacksonMapperViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.parsingXML()

    }
}