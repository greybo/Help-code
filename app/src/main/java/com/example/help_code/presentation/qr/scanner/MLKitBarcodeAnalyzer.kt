package com.example.help_code.presentation.qr.scanner

import android.graphics.Rect
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber
import java.util.Date

class MLKitBarcodeAnalyzer(
    private val listener: ScanningResultListener,
) : ImageAnalysis.Analyzer {

    private var isScanning: Boolean = false
    var rectOverlay: Rect? = null
    var timestamp: Long = 0
    private var targetRotation: Int = 0

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null && !isScanning) {
            targetRotation = imageProxy.imageInfo.rotationDegrees
            val image = InputImage.fromMediaImage(mediaImage, targetRotation)
            // Pass image to an ML Kit Vision API
            // ...
            val scanner = BarcodeScanning.getClient()

            isScanning = true
//            scanner.
            scanner.process(image).addOnSuccessListener { barcodes ->
                // Task completed successfully
                // ...
                barcodes.firstOrNull().let { barcode ->
//                    listener.setPoints(barcode?.cornerPoints)
                    val withOffset = plusOffset(barcode?.boundingBox)
//                    val withOffset = plusOffset(rotate(barcode?.boundingBox))
                    listener.setDynamicRect(withOffset)
                    val rawValue = barcode?.rawValue
                    if (Date().time > timestamp && rectContains(rectOverlay, withOffset)) {
                        timestamp = Date().time + 1000
                        rawValue?.let {
                            Timber.e("Barcode: $it")
                            listener.onScanned(it)
                        }
                    }
                }

                isScanning = false
                imageProxy.close()
            }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                    isScanning = false
                    imageProxy.close()
                }
        }
    }

    private fun rectContains(staticRect: Rect?, dynamicRect: Rect?): Boolean {
        staticRect ?: return false
        dynamicRect ?: return false
        val rotateRect = dynamicRect
        return staticRect.left < rotateRect.left &&
                staticRect.top < rotateRect.top &&
                staticRect.right > rotateRect.right &&
                staticRect.bottom > rotateRect.bottom
    }

    private fun plusOffset(rect: Rect?): Rect? {
        rect ?: return null
        Timber.d("plusOffset rect: $rect")
        Timber.d("plusOffset rotate: $targetRotation")
        val offset = 50
        return Rect(
            rect.left - offset,
            rect.top - offset,
            rect.right + offset,
            rect.bottom + offset
        )
    }

    private fun rotate(rect: Rect?): Rect? {
        rect ?: return null
        Timber.d("Rotate is: $targetRotation")
        return when (targetRotation) {
            180 -> Rect(rect.top, rect.right, rect.bottom, rect.left)
            90 -> Rect(rect.right, rect.bottom, rect.left, rect.top)
            0 -> Rect(rect.bottom, rect.left, rect.top, rect.right)
            /* 270  */else -> rect
        }
    }
}