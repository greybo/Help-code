package com.example.help_code.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.help_code.HelpCodeApplication
import com.example.help_code.MainRouter
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentMainBinding
import com.example.help_code.presentation.scannerOld.LivePreviewActivity

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
        binding.mainRecyclerView.adapter = MainAdapter(FragmentNameEnum.values()) {
            if (it == FragmentNameEnum.ScannerFragmentOld) {
                startActivity(Intent(HelpCodeApplication.instance.applicationContext, LivePreviewActivity::class.java))
//                val intent = Intent(HelpCodeApplication.instance.applicationContext, LivePreviewActivity::class.java)
////                intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.LIVE_PREVIEW)
//                startActivity(intent)
            } else route.navigation(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

