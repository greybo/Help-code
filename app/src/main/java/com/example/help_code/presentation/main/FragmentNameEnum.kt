package com.example.help_code.presentation.main

enum class FragmentNameEnum(val rawValue: String) {
    DropDown("InputTextViews"),
    PagerFragment("Pager Fragment"),
    ScannerFragment("Scanner QR"),
    Video("ExoPlayer"),
    Behavior("Behavior"),
    Swipe("Swipe"),
    FormattingPhone("Formatting phone"),
    TooltipFragment("Tooltip"),
    ReactiveFragment("Reactive"),
    ;

    companion object {
        fun getList(): MutableList<String> {
            return values().map { it.rawValue }.toMutableList()
        }
    }

}