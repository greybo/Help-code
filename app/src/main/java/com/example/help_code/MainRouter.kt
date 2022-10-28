package com.example.help_code

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.help_code.presentation.main.FragmentName

class MainRouter(private val controller: NavController) {

    fun navigation(name: FragmentName) {
        when (name) {
            FragmentName.DropDown -> controller.navigate(R.id.toDropDownListFragment)
            FragmentName.PagerFragment -> controller.navigate(
                R.id.toPagerMainFragment,
                bundleOf("tab_number" to "2")
            )
            FragmentName.ScannerFragment -> controller.navigate(R.id.toBarcodeScanningFragment)
            FragmentName.Video -> controller.navigate(R.id.toVideoPlayerFragment)
            else -> TODO()
        }
    }

    fun onBackPress() {
        controller.popBackStack()
    }
}