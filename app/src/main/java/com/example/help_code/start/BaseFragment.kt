package com.example.help_code.start

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract val route: CodeHelpRoute
    abstract fun initToolbar(layout: ActionBarView)

    val homeToolbarCallback = {
        route.onBackPress()
    }
}