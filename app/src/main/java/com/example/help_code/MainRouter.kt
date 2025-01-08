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
            FragmentNameEnum.ScannerFragmentGms -> controller.navigate(R.id.toGmsBarcodeScannerFragment)
            FragmentNameEnum.ScannerFragmentActivity -> controller.navigate(R.id.toQrScannerActivity)
            FragmentNameEnum.Video -> controller.navigate(R.id.toVideoPlayerFragment)
            FragmentNameEnum.Behavior -> controller.navigate(R.id.toBehaviorFragment)
            FragmentNameEnum.Swipe -> controller.navigate(R.id.toSwipeFragment)
            FragmentNameEnum.FormattingPhone -> controller.navigate(R.id.toPhoneFormatting)
            FragmentNameEnum.TooltipFragment -> controller.navigate(R.id.toTooltipFragment)
            FragmentNameEnum.ReactiveFragment -> controller.navigate(R.id.toReactiveFragment)
            FragmentNameEnum.BiometricFragment -> controller.navigate(R.id.toBiometricFragment)
            FragmentNameEnum.CoroutineFragment -> controller.navigate(R.id.toCollectionCoroutineFragment)
            FragmentNameEnum.WebViewCameraFragment -> controller.navigate(R.id.toWebViewCameraFragment)
            FragmentNameEnum.JacksonFragment -> controller.navigate(R.id.toJacksonFragment)
            FragmentNameEnum.BlankFragment -> controller.navigate(R.id.toBlankFragment)
            FragmentNameEnum.OpenIntentFragment -> controller.navigate(R.id.toOpenIntentFragment)
            else -> TODO()
        }
    }

    fun onBackPress() {
        controller.popBackStack()
    }
}