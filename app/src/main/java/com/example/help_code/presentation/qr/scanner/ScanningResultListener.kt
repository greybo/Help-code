package com.example.help_code.presentation.qr.scanner

import android.graphics.Rect

interface ScanningResultListener {
    fun onScanned(result: String)
//    fun setPoints(result: Array<Point>?)
    fun setDynamicRect(rect: Rect?)
}