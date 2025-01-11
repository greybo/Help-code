package com.example.help_code.presentation.biometric

sealed class BiometricItems(val itemText: String) {

    data class CanType(val type: BiometricFragment.BiometricType) : BiometricItems(type.name)
    class CheckBiometric : BiometricItems("Check biometric")
    class OpenBiometricSettings : BiometricItems("Open settings")
}