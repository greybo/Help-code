package com.example.help_code.presentation.biometric

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.help_code.HelpCodeApplication
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.BiometricFragmentBinding

@RequiresApi(Build.VERSION_CODES.R)
class BiometricFragment : BaseBindingFragment<BiometricFragmentBinding>(BiometricFragmentBinding::inflate) {
    val REQUEST_CODE: Int = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    toast("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    toast("Authentication succeeded!")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    toast("Authentication failed")
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        binding.biometricButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
        binding.biometricButtonCheck.setOnClickListener {
            checkBiometric()
        }
    }

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        toast("registerForActivityResult: ${it.resultCode}")
    }

    private fun checkBiometric() {
        val biometricManager = BiometricManager.from(requireContext())
        val messageAllowed = when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                "App can authenticate using biometrics."

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                "No biometric features available on this device."

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                "Biometric features are currently unavailable."

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                launcher.launch(enrollIntent)
                "BIOMETRIC_ERROR_NONE_ENROLLED"
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED"
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                "BIOMETRIC_ERROR_UNSUPPORTED"
            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                "BIOMETRIC_STATUS_UNKNOWN"
            }

            else -> {
                "UNKNOWN"
            }
        }
        toast(messageAllowed)
    }

    private fun toast(message: String) {
        Toast.makeText(HelpCodeApplication.instance, message, Toast.LENGTH_LONG).show()
        Log.d("MY_APP_TAG", message)
    }
}