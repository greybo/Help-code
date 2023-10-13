package com.example.help_code.presentation.biometric

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.BiometricFragmentBinding
import java.nio.charset.Charset
import java.util.concurrent.Executor


class BiometricFragment : BaseBindingFragment<BiometricFragmentBinding>(BiometricFragmentBinding::inflate) {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val listAllowedBiometric = mutableListOf<BiometricItems>()
    private val stringBuffer = mutableListOf<String>()
    private val biometricManager by lazy { BiometricManager.from(requireContext()) }


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        toast("registerForActivityResult: ${it.resultCode}")
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkBiometricAllowed()

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    toast("Authentication error: $errorCode :: $errString")
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        toast("loginWithPassword()") // Because negative button says use application password
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    // plaintext-string text is whatever data the developer would like
                    // to encrypt. It happens to be plain-text in this example, but it
                    // can be anything
                    val encryptedInfo: ByteArray? = result.cryptoObject?.cipher?.doFinal(
                        "plaintext-string".toByteArray(Charset.defaultCharset())
                    )
                    toast("Authentication succeeded! - $encryptedInfo")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    toast("Authentication failed")
                }
            })
    }


    enum class BiometricType(val idType: Int) {
        BiometricWeak(BIOMETRIC_WEAK),
        BiometricStrong(BIOMETRIC_STRONG),
        DeviceCredential(DEVICE_CREDENTIAL);

        companion object {
            fun get() = values().map { it.idType }
        }

        fun getPromptInfo(isDeviceCredential: Boolean): BiometricPrompt.PromptInfo {
            val prompt = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setAllowedAuthenticators(idType)

            val negativeText = if (isDeviceCredential) "Use devise password" else "cancel"
            return when (this) {
                BiometricStrong,
                BiometricWeak -> prompt.setNegativeButtonText(negativeText)

                DeviceCredential -> prompt
            }.build()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkBiometricAllowed() {
        val list = BiometricType.values().filter {
            biometricManager.canAuthenticate(it.idType).run {
                toast("${it.name}: ${getState(this)}")
                this == BiometricManager.BIOMETRIC_SUCCESS
            }
        }
        adapterUpdate(list)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun adapterUpdate(listAllowedBiometric: List<BiometricType>) {
        val credential = listAllowedBiometric.contains(BiometricType.DeviceCredential)
        toast("allowed credential: $credential")

        val items = mutableListOf(
            BiometricItems.CheckBiometric(),
            BiometricItems.OpenBiometricSettings()
        ).apply {
            addAll(listAllowedBiometric.map { BiometricItems.CanType(it) })
        }

        val adapter = BiometricAdapter(items) {
            when (it) {
                is BiometricItems.CanType -> biometricPrompt.authenticate(it.type.getPromptInfo(credential))
                is BiometricItems.CheckBiometric -> checkBiometricAllowed()
                is BiometricItems.OpenBiometricSettings -> openBiometricSetting()
                else -> {}
            }
        }

        binding.biometricRecyclerView.adapter = adapter
    }

    private fun getState(result: Int) = when (result) {
        BiometricManager.BIOMETRIC_SUCCESS -> "App can authenticate using biometrics."
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "No biometric features available on this device."
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Biometric features are currently unavailable."
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "BIOMETRIC_ERROR_NONE_ENROLLED" //open biometric setting -> openBiometricSetting()
        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED"
        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> "BIOMETRIC_ERROR_UNSUPPORTED"
        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> "BIOMETRIC_STATUS_UNKNOWN"
        else -> "UNKNOWN"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun openBiometricSetting() {
        // Prompts the user to create credentials that your app accepts.
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(
                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            )
        }
        launcher.launch(enrollIntent)
    }

    private fun toast(message: String) {
        stringBuffer.add("\n-$message")

//        Toast.makeText(HelpCodeApplication.instance, message, Toast.LENGTH_LONG).show()
        Log.d("MY_APP_TAG", message)
        binding.biometricLog.text = stringBuffer.asReversed().joinToString(" "/*, prefix = "Start\n"*/)
    }
}