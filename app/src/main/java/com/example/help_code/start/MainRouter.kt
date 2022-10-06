package com.example.help_code.start

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.help_code.R

class MainRouter(private val controller: NavController) {

    fun navigation(name: String) {
        when (FragmentName.values().find { it.rawValue == name }) {
            FragmentName.DropDown -> controller.navigate(R.id.toDropDownListFragment)
            FragmentName.PagerFragment -> controller.navigate(
                R.id.toPagerMainFragment,
                bundleOf("tab_number" to "2")
            )
            else -> {}
        }
    }

    fun onBackPress() {
        controller.popBackStack()
    }
}