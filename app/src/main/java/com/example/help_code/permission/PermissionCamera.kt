package com.example.help_code.permission

import android.Manifest.permission.CAMERA
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun Fragment.cameraPermissionLaunch(callback: (Boolean) -> Unit) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        callback(it)
    }.launch(CAMERA)
}

val Fragment.shouldShowPermissionCamera: Boolean
    get() = shouldShowRequestPermissionRationale(CAMERA)

val Fragment.isPermissionCamera: Boolean
    get() = (ContextCompat.checkSelfPermission(
        this.requireContext(),
        CAMERA
    ) == PackageManager.PERMISSION_GRANTED)


fun FragmentActivity.cameraPermissionLaunch(callback: (Boolean) -> Unit) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        callback(it)
    }.launch(CAMERA)
}

val FragmentActivity.isPermissionCamera: Boolean
    get() = (ContextCompat.checkSelfPermission(
        this,
        CAMERA
    ) == PackageManager.PERMISSION_GRANTED)

