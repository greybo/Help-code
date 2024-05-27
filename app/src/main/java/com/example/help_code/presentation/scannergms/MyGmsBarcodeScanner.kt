package com.example.help_code.presentation.scannergms

import android.content.Context
import com.example.help_code.utilty.dialog.LoadingDialog
import com.example.help_code.utilty.toast
import com.google.android.gms.common.api.OptionalModuleApi
import com.google.android.gms.common.moduleinstall.InstallStatusListener
import com.google.android.gms.common.moduleinstall.ModuleAvailabilityResponse
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_CANCELED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.InstallState.STATE_FAILED
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import timber.log.Timber


fun bindBarcodeScanning(context: Context, callback: (String?) -> Unit) {
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .enableAutoZoom() // available on 16.1.0 and higher
        .build()

    val scanner = GmsBarcodeScanning.getClient(context, options)

    scanner.startScan()
        .addOnSuccessListener { barcode ->
            Timber.e("addOnSuccessListener: ")
            val rawValue = barcode?.rawValue
            val boundingBox = barcode?.boundingBox
            Timber.e("Barcode: $rawValue")
            Timber.e("boundingBox: $boundingBox")
            callback(rawValue)
        }
        .addOnCanceledListener {
            Timber.e("addOnCanceledListener")
        }
        .addOnFailureListener { e ->
            Timber.e("addOnFailureListener: ${e.message}")
        }
}

class GmsScanner(var context: Context?) {
    private fun myContext(): Context {
        return context ?: throw Throwable()
    }

    fun cancel() {
        context = null
    }

    private val moduleInstallClient = ModuleInstall.getClient(myContext())
    fun onScanButtonClicked() {
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
            .allowManualInput()
            .enableAutoZoom()


        val optionalModuleApi: OptionalModuleApi = GmsBarcodeScanning.getClient(myContext())
        moduleInstallClient
            .areModulesAvailable(optionalModuleApi)
            .addOnSuccessListener { response: ModuleAvailabilityResponse ->
                if (response.areModulesAvailable()) {
                    // Modules are present on the device...
                    myContext().toast("Modules are present on the device")
                    val gmsBarcodeScanner = GmsBarcodeScanning.getClient(
                        myContext(),
                        optionsBuilder.build()
                    )
                    gmsBarcodeScanner
                        .startScan()
                        .addOnSuccessListener { barcode: Barcode? ->

                            myContext().toast(barcode?.rawValue ?: "1")

                        }
                        .addOnFailureListener { e: Exception? ->
                            myContext().toast(e?.message ?: "2")

                        }
                        .addOnCanceledListener { myContext().toast("Error scan") }
                } else {
                    // Modules are not present on the device...
                    myContext().toast("Modules are not present on the device")
                    moduleInstall()
                }
            }
            .addOnFailureListener { e: Exception? ->
                // Handle failure…
                myContext().toast("Handle failure…")
            }
    }


    inner class ModuleInstallProgressListener : InstallStatusListener {
        override fun onInstallStatusUpdated(update: ModuleInstallStatusUpdate) {
            val progressInfo = update.progressInfo
            // Progress info is only set when modules are in the progress of downloading.
            if (progressInfo != null) {
                val progress =
                    (progressInfo.bytesDownloaded * 100 / progressInfo.totalBytesToDownload).toInt()
                // Set the progress for the progress bar.
//                progressBar.setProgress(progress)
                LoadingDialog(myContext(), progress.toString())
            }
            // Handle failure status maybe…

            // Unregister listener when there are no more install status updates.
            if (isTerminateState(update.installState)) {
                moduleInstallClient.unregisterListener(this)
            }
        }

        fun isTerminateState(@InstallState state: Int): Boolean {
            return state == STATE_CANCELED || state == STATE_COMPLETED || state == STATE_FAILED
        }
    }


    private fun moduleInstall() {
        val listener: InstallStatusListener = ModuleInstallProgressListener()
        val optionalModuleApi: OptionalModuleApi = GmsBarcodeScanning.getClient(myContext())
        val moduleInstallRequest = ModuleInstallRequest.newBuilder()
            .addApi(optionalModuleApi) // Add more API if you would like to request multiple optional modules
            //.addApi(...)
            // Set the listener if you need to monitor the download progress
            .setListener(listener)
            .build()
        moduleInstallClient.installModules(moduleInstallRequest)
            .addOnSuccessListener { response ->
                if (response.areModulesAlreadyInstalled()) {
                    // Modules are already installed when the request is sent.
                    myContext().toast("Modules are already installed when the request is sent.")
                }
            }
            .addOnFailureListener { e ->
                // Handle failure...
                myContext().toast(e.message ?: "Error scan 2")
            }
    }
}
