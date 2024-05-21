package com.example.help_code.presentation.scanner

import android.graphics.Rect
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import timber.log.Timber
import java.util.Date

class MLKitBarcodeAnalyzer(
    private val listener: ScanningResultListener,
    val scope: CoroutineScope = GlobalScope
) : ImageAnalysis.Analyzer {

    private var isScanning: Boolean = false
    var rectOverlay: Rect? = null
    var timestamp: Long = 0

    @ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image

        if (mediaImage != null && !isScanning) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            val scanner = BarcodeScanning.getClient()

            isScanning = true
            scanner.process(image).addOnSuccessListener { barcodes ->
                // Task completed successfully
                // ...
                barcodes.firstOrNull().let { barcode ->
//                    listener.setPoints(barcode?.cornerPoints)
                    val withOffset = plusOffset(barcode?.boundingBox)
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
        Timber.d("Rotate is: $rect")
        val offset = 200
        return Rect(
            rect.left - offset,
            rect.top - offset,
            rect.right + offset,
            rect.bottom + offset
        )
    }
}