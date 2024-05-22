package com.example.help_code.presentation.scanner2

import android.content.Context
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
            Timber.e("Barcode: $rawValue")
            callback(rawValue)
        }
        .addOnCanceledListener {
            Timber.e("addOnCanceledListener")
        }
        .addOnFailureListener { e ->
            Timber.e("addOnFailureListener: ${e.message}")
        }
}