package com.example.help_code.presentation.qr.scanner2

import android.os.Bundle
import android.view.View
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentWalletQrScanner2Binding
import com.example.help_code.permission.cameraPermissionLaunch
import com.example.help_code.permission.isPermissionCamera
import com.example.help_code.utilty.toast
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WalletQrScannerFragment2 :
    BaseBindingFragment<FragmentWalletQrScanner2Binding>(FragmentWalletQrScanner2Binding::inflate) {

    private var cameraExecutor: ExecutorService? = null
    private var barcodeScanner: BarcodeScanner? = null
    private val resultLiveData = MutableLiveData<String?>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultLiveData.observe(viewLifecycleOwner) {
            setFragmentResult("QR_KEY_REQUEST_FRAGMENT", bundleOf("QR_KEY_RESULT" to it))
            findNavController().popBackStack()
        }

        binding.walletQrScanClose.setOnClickListener {
            findNavController().popBackStack()
        }

        if (isPermissionCamera) {
            startCamera()
        } else cameraPermissionLaunch {
            if (it) {
                startCamera()
            } else {
                toast("Camera Permission Denied")
                findNavController().popBackStack()
            }
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        var cameraController = LifecycleCameraController(requireContext())
        val previewView: PreviewView = binding.viewFinder

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(requireContext()),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(requireContext())
            ) { result: MlKitAnalyzer.Result? ->
                val barcodeResults = barcodeScanner?.let { result?.getValue(it) }
                if ((barcodeResults == null) ||
                    (barcodeResults.size == 0) ||
                    (barcodeResults.first() == null)
                ) {
                    previewView.overlay.clear()
                    previewView.setOnTouchListener { _, _ -> false } //no-op
                    return@MlKitAnalyzer
                }
                val qrCodeEntity = QrCodeEntity(barcodeResults[0])
                val qrCodeDrawable = QrCodeDrawable(qrCodeEntity)
                previewView.setOnTouchListener(qrCodeEntity.qrCodeTouchCallback)
                previewView.overlay.clear()
                previewView.overlay.add(qrCodeDrawable)
            }
        )

        cameraController.bindToLifecycle(this)
        previewView.controller = cameraController
    }

    override fun onDestroyView() {
        cameraExecutor?.shutdown()
        barcodeScanner?.close()
        super.onDestroyView()
    }



}