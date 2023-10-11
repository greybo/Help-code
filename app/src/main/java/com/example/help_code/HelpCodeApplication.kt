package com.example.help_code

import android.app.Application
import timber.log.Timber

class HelpCodeApplication : Application() {

    companion object {
        lateinit var instance: HelpCodeApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
    }
}