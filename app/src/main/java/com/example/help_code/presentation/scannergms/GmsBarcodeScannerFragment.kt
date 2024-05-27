package com.example.help_code.presentation.scannergms

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentGmsBarcodeScanningBinding
import com.example.help_code.permission.cameraPermissionLaunch
import com.example.help_code.permission.isPermissionCamera

class GmsBarcodeScannerFragment :
    BaseBindingFragment<FragmentGmsBarcodeScanningBinding>(FragmentGmsBarcodeScanningBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isPermissionCamera) {
            startCamera()
        } else cameraPermissionLaunch {
            if (it) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }
    }

    private fun startCamera() {
        binding.gmsScanButton.setOnClickListener {
            bindBarcodeScanning(requireContext()) { barcode ->
                binding.gmsScanText.text = barcode
            }
        }
    }
}
