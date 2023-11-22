package com.example.help_code.presentation.blank

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlankBinding
import com.example.help_code.presentation.blank.coroutine.LatestNewsViewModel

class BlankFragment : BaseBindingFragment<FragmentBlankBinding>(FragmentBlankBinding::inflate) {

    private val viewModel: BlankViewModel by viewModels()
    private val viewModel2: LatestNewsViewModel by viewModels()
    private val viewModel3: BlankViewModel3 by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel3.runTask()
//        viewModel2.fetch()
//        binding.blankFragmentButton.setOnClickListener { viewModel.runTask() }

    }
}
