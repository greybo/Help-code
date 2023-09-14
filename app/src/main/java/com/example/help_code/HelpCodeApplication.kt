package com.example.help_code

import android.app.Application
import timber.log.Timber

class HelpCodeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}