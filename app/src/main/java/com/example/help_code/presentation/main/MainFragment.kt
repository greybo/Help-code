package com.example.help_code.presentation.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.help_code.MainRouter
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentMainBinding

class MainFragment : BaseBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val route: MainRouter by lazy { MainRouter(findNavController()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainToolbar.getBuilder()
            .homeCallback {
                route.onBackPress()
            }
            .create()
        initAdapter()
    }

    private fun initAdapter() {
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.mainRecyclerView.adapter =
            com.example.help_code.presentation.main.MainAdapter(FragmentNameEnum.values()) {
                route.navigation(it)
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

