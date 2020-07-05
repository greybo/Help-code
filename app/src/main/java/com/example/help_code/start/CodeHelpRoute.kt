package com.example.help_code.start

import androidx.navigation.NavController
import com.example.help_code.R

class CodeHelpRoute(private val controller: NavController) {

    fun navigation(name: String) {
        when (FragmentName.values().find { it.rawValue == name }) {
            FragmentName.DropDown -> controller.navigate(R.id.dropDownListFragment)
        }
    }
}