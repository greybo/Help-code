package com.example.help_code.base

import androidx.fragment.app.Fragment
import com.example.help_code.start.ActionBarView
import com.example.help_code.start.CodeHelpRoute

abstract class BaseFragment : Fragment() {
    abstract val route: CodeHelpRoute
    abstract fun initToolbar(layout: ActionBarView)

    val homeToolbarCallback = {
        route.onBackPress()
    }
}