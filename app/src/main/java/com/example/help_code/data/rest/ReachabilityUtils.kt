package com.example.help_code.data.rest

import android.content.Context
import android.net.ConnectivityManager
import com.example.help_code.HelpCodeApplication
import com.example.help_code.utilty.dialog.presentNetworkErrorAlert


fun hasInternetConnection(): Boolean {
    val ctx = HelpCodeApplication.instance
    val connMgr = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    if (connMgr != null) {
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
    }

    return false
}

fun showNoInternetAlertIfNeed(context: Context?): Boolean {
    if (!hasInternetConnection()) {
        presentNetworkErrorAlert(context)
        return true
    }
    return false
}
