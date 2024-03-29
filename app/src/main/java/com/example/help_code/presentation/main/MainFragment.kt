package com.example.help_code.presentation.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.help_code.MainRouter
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val route: MainRouter by lazy {
        MainRouter(findNavController())
    }

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
        mainRecyclerView.layoutManager = LinearLayoutManager(context)
        mainRecyclerView.adapter = MainAdapter(FragmentName.values()) {
            route.navigation(it)
        }
    }
}

enum class FragmentName(val rawValue: String) {
    DropDown("InputTextViews"),
    PagerFragment("Pager Fragment"),
    ScannerFragment("Scanner QR"),
    Video("ExoPlayer"),
    Behavior("Behavior"),
    Swipe("Swipe"),
    FormattingPhone("Formatting phone"),
    ;

    companion object {
        fun getList(): MutableList<String> {
            return values().map { it.rawValue }.toMutableList()
        }
    }

}