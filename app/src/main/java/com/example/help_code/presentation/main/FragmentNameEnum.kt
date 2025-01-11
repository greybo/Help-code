package com.example.help_code.presentation.main

enum class FragmentNameEnum(val rawValue: String) {
    BlankFragment("Blank"),
    ConvertToExelFragment("Convert to exel"),
    OpenIntentFragment("Open intent"),
    JacksonFragment("Jackson parser"),
    DropDown("InputTextViews"),
    PagerFragment("Pager Fragment"),
    ScannerFragment("Scanner QR"),
    ScannerFragmentGms("Scanner QR GMS"),
    ScannerFragmentOld("Scanner OLD"),
    ScannerFragmentActivity("Scanner Activity"),
    Video("ExoPlayer"),
    Behavior("Behavior"),
    Swipe("Swipe"),
    FormattingPhone("Formatting phone"),
    TooltipFragment("Tooltip"),
    ReactiveFragment("Reactive"),
    BiometricFragment("Biometric"),
    CoroutineFragment("Coroutine"),
    WebViewCameraFragment("WebView Camera"),
    ;

    companion object {
        fun getList(): MutableList<String> {
            return values().map { it.rawValue }.toMutableList()
        }
    }

}