package com.example.help_code.presentation.scanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.help_code.R
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class QrScannerActivity : AppCompatActivity() {
    private var previewView: PreviewView? = null
    private var overlay: ViewFinderOverlayScan? = null
    private var cameraSelector: CameraSelector? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner_layout)
        previewView = findViewById(R.id.previewView)
        overlay = findViewById(R.id.overlay)
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    fun startCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                CAMERA_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupCamera()
        } else {
            permissions
        }
    }

    private val permissions: Unit
        private get() {
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA_PERMISSION), PERMISSION_CODE)
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        for (r in grantResults) {
            if (r == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                return
            }
        }
        if (requestCode == PERMISSION_CODE) {
            setupCamera()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val lensFacing = CameraSelector.LENS_FACING_BACK
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindAllCameraUseCases()
            } catch (e: ExecutionException) {
                Log.e(TAG, "cameraProviderFuture.addListener Error", e)
            } catch (e: InterruptedException) {
                Log.e(TAG, "cameraProviderFuture.addListener Error", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            cameraProvider?.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }
        val builder = Preview.Builder()
        builder.setTargetRotation(rotation)
        previewUseCase = builder.build()
        previewUseCase!!.setSurfaceProvider(previewView!!.getSurfaceProvider())
        try {
            cameraProvider!!
                .bindToLifecycle(this, cameraSelector!!, previewUseCase)
        } catch (e: Exception) {
            Log.e(TAG, "Error when bind preview", e)
        }
    }

    private fun bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider?.unbind(analysisUseCase)
        }
        val cameraExecutor: Executor = Executors.newSingleThreadExecutor()
        val builder = ImageAnalysis.Builder()
        builder.setTargetRotation(rotation)
        analysisUseCase = builder.build()
        analysisUseCase?.setAnalyzer(cameraExecutor) { image: ImageProxy -> analyze(image) }
        try {
            cameraProvider!!
                .bindToLifecycle(this, cameraSelector!!, analysisUseCase)
        } catch (e: Exception) {
            Timber.e("Error when bind analysis", e)
        }
    }

    @get:Throws(NullPointerException::class)
    protected val rotation: Int
        protected get() = previewView!!.display.rotation

    @SuppressLint("UnsafeOptInUsageError")
    private fun analyze(image: ImageProxy) {
        if (image.image == null) return
        val inputImage = InputImage.fromMediaImage(
            image.image!!,
            image.imageInfo.rotationDegrees
        )
        val barcodeScanner = BarcodeScanning.getClient()
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes: List<Barcode> -> onSuccessListener(barcodes) }
            .addOnFailureListener { e: Exception? -> Log.e(TAG, "Barcode process failure", e) }
            .addOnCompleteListener { task: Task<List<Barcode?>?>? -> image.close() }
    }

    private fun onSuccessListener(barcodes: List<Barcode>) {
        if (barcodes.isNotEmpty()) {
            val rect: Rect? = barcodes[0].boundingBox
            overlay?.setDynamicRect(rect)
            Toast.makeText(this, barcodes[0].displayValue, Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        private const val TAG = "MLKit Barcode"
        private const val PERMISSION_CODE = 1001
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}