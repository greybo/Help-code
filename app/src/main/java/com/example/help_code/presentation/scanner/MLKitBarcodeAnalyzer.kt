package com.example.help_code.presentation.scanner

import android.graphics.Rect
import android.view.Surface
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber

class MLKitBarcodeAnalyzer(private val listener: ScanningResultListener) : ImageAnalysis.Analyzer {

    private var isScanning: Boolean = false
    var rectOverlay: Rect? = null
    var targetRotation: Int = 0

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null && !isScanning) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            val scanner = BarcodeScanning.getClient()

            isScanning = true
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    // ...
                    barcodes.firstOrNull().let { barcode ->
                        listener.setPoints(barcode?.cornerPoints)
                        val rawValue = barcode?.rawValue
                        if (rectContains(rectOverlay, barcode?.boundingBox)) {
                            rawValue?.let {
                                Timber.e("Barcode: $it")
//                                listener.onScanned(it)
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
        val offset = 1.2

        staticRect ?: return false
        dynamicRect ?: return false
        val rotateRect = rotate(dynamicRect)
        Timber.d("Rect staticRect: $staticRect")
        Timber.d("Rect rotateRect: $rotateRect")
        return (staticRect.left + offset) < rotateRect.left &&
                (staticRect.top + offset) < rotateRect.top &&
                (staticRect.right - offset) > rotateRect.right &&
                (staticRect.bottom - offset) > rotateRect.bottom
    }

    private fun rotate(rect: Rect): Rect {
        Timber.d("Rotate is: $targetRotation")
        return when (targetRotation) {
            Surface.ROTATION_270 -> Rect(rect.bottom, rect.left, rect.top, rect.right)
            Surface.ROTATION_180 -> Rect(rect.right, rect.bottom, rect.left, rect.top)
            Surface.ROTATION_90 -> Rect(rect.top, rect.right, rect.bottom, rect.left)
            else -> rect
        }
    }
}