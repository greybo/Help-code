package com.example.help_code

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.help_code.presentation.main.FragmentNameEnum

class MainRouter(private val controller: NavController) {

    fun navigation(name: FragmentNameEnum) {
        when (name) {
            FragmentNameEnum.DropDown -> controller.navigate(R.id.toDropDownListFragment)
            FragmentNameEnum.PagerFragment -> controller.navigate(
                R.id.toPagerMainFragment,
                bundleOf("tab_number" to "2")
            )
            FragmentNameEnum.ScannerFragment -> controller.navigate(R.id.toBarcodeScanningFragment)
            FragmentNameEnum.Video -> controller.navigate(R.id.toVideoPlayerFragment)
            FragmentNameEnum.Behavior -> controller.navigate(R.id.toBehaviorFragment)
            FragmentNameEnum.Swipe -> controller.navigate(R.id.toSwipeFragment)
            FragmentNameEnum.FormattingPhone -> controller.navigate(R.id.toPhoneFormatting)
            FragmentNameEnum.TooltipFragment -> controller.navigate(R.id.toTooltipFragment)
            FragmentNameEnum.ReactiveFragment -> controller.navigate(R.id.toReactiveFragment)
            FragmentNameEnum.BiometricFragment -> controller.navigate(R.id.toBiometricFragment)
            else -> TODO()
        }
    }

    fun onBackPress() {
        controller.popBackStack()
    }
}