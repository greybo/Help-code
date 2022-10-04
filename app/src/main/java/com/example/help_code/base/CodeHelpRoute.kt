package com.example.help_code.base

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

abstract class CodeHelpRoute() {
    lateinit var navController: NavController
}
