package com.example.help_code

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import timber.log.Timber


class HelpCodeApplication : Application() {
    private val LOG_TAG = "SampleApplication"

    companion object {
        lateinit var instance: HelpCodeApplication
    }


    override fun onCreate() {
//        setStrictMode()
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())

//        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
//            param(FirebaseAnalytics.Param.ITEM_ID, id);
//            param(FirebaseAnalytics.Param.ITEM_NAME, name);
//            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        }
    }

    private fun setStrictMode() {
        Timber.tag(LOG_TAG).d("Enabling StrictMode policy over Sample application")
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
    }
}