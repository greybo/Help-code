package com.example.help_code.presentation.scanner

import android.graphics.Point

interface ScanningResultListener {
    fun onScanned(result: String)
    fun setPoints(result: Array<Point>?)
}