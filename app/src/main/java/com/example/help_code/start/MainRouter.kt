package com.example.help_code.start

import androidx.navigation.NavController
import com.example.help_code.R
import com.example.help_code.base.CodeHelpRoute

class MainRouter(private val controller: NavController): CodeHelpRoute() {

    fun navigation(name: String) {
        when (FragmentName.values().find { it.rawValue == name }) {
            FragmentName.DropDown -> controller.navigate(R.id.dropDownListFragment)
            FragmentName.PagerFragment -> controller.navigate(R.id.toPagerMainFragment)
            else -> {}
        }
    }

    fun onBackPress() {
        controller.popBackStack()
    }
}