package com.example.help_code

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.main_host_fragment)

    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.mainFragment)
            super.onBackPressed()
        else navController.popBackStack()
    }
}
