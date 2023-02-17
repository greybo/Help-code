package com.example.help_code.presentation.scanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.help_code.databinding.FragmentBarcodeScannerResultListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

const val ARG_SCANNING_RESULT = "scanning_result"

class ScannerResultDialog(private val listener: DialogDismissListener) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBarcodeScannerResultListDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBarcodeScannerResultListDialogBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val scannedResult = arguments?.getString(ARG_SCANNING_RESULT)
        binding.edtResult.setText(scannedResult)
        binding.btnCopy.setOnClickListener {
            val clipboard = ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
            val clip = ClipData.newPlainText("label",scannedResult)
            clipboard?.setPrimaryClip(clip)
//            Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
            dismissAllowingStateLoss()
        }
    }


    companion object {

        fun newInstance(scanningResult: String, listener: DialogDismissListener): ScannerResultDialog =
            ScannerResultDialog(listener).apply {
                arguments = Bundle().apply {
                    putString(ARG_SCANNING_RESULT, scanningResult)
                }
            }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        listener.onDismiss()
    }

    interface DialogDismissListener {
        fun onDismiss()
    }
}