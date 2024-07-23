package com.example.help_code.presentation.qr.scannergms

import android.os.Bundle
import android.view.View
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentGmsBarcodeScanningBinding

class GmsBarcodeScannerFragment :
    BaseBindingFragment<FragmentGmsBarcodeScanningBinding>(FragmentGmsBarcodeScanningBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        startCamera()
        scanner.onScanButtonClicked()
//        if (isPermissionCamera) {
//            startCamera()
//        } else cameraPermissionLaunch {
//            if (it) {
//                startCamera()
//            } else {
//                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT)
//                    .show()
//                findNavController().popBackStack()
//            }
//        }
    }

    val scanner by lazy { GmsScanner(requireContext()) }
    private fun startCamera() {

        binding.gmsScanButton.setOnClickListener {
            scanner.onScanButtonClicked()
//            bindBarcodeScanning(requireContext()) { barcode ->
//                binding.gmsScanText.text = barcode
//            }
        }
    }

    override fun onDestroyView() {
        scanner.cancel()
        super.onDestroyView()
    }
}
