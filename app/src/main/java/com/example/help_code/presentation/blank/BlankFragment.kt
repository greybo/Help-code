package com.example.help_code.presentation.blank

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlankBinding

class BlankFragment : BaseBindingFragment<FragmentBlankBinding>(FragmentBlankBinding::inflate) {

    private val viewModel: BlankViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.runTask()
        binding.blankFragmentButton.setOnClickListener { viewModel.runTask() }

    }
}
