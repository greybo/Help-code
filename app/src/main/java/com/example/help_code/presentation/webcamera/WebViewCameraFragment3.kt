package com.example.help_code.presentation.webcamera

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentWebViewCameraBinding
import timber.log.Timber

private const val REQUEST_CAMERA_PERMISSION: Int = 1
class WebViewCameraFragment :
    BaseBindingFragment<FragmentWebViewCameraBinding>(FragmentWebViewCameraBinding::inflate) {

    @SuppressLint("JavascriptInterface")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.apply {
            webViewClient = customWebViewClient
            webChromeClient = customWebChromeClient

            settings.apply {
                javaScriptEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                javaScriptCanOpenWindowsAutomatically = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                setSupportZoom(true)
                setAllowFileAccessFromFileURLs(true);
                setAllowUniversalAccessFromFileURLs(true);
                setBuiltInZoomControls(true);
            }

//            loadUrl("file:///android_asset/index.html");
            loadUrl("https://dmytroyavorskyi.github.io")
//            setWebViewClient(object : WebViewClient() {
//                override fun onReceivedError(
//                    view: WebView,
//                    request: WebResourceRequest,
//                    error: WebResourceError
//                ) {
//                    Log.e("WebViewError", "Error: " + error.description)
//                }
//            })

        }
    }


    private val customWebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
//            listener?.showLoading(true)
            Timber.d("onPageStarted: $url")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
//            listener?.showLoading(false)
            Timber.d("onPageFinished: $url")

        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            Timber.e("onReceivedError",error)

//            listener?.showLoading(false)
        }
    }

    private val customWebChromeClient = object : WebChromeClient() {

        override fun onPermissionRequest(request: PermissionRequest?) {
            super.onPermissionRequest(request)
//            request?.grant(request.resources);

            Timber.d("onPermissionRequest: ${request?.resources}")
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf( Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION);
            } else {
                // Запустить JavaScript-код для открытия камеры
                Timber.d("onPermissionRequest: granted")
            }
        }

        override fun onPermissionRequestCanceled(request: PermissionRequest?) {
            super.onPermissionRequestCanceled(request)
            request?.deny()
            Timber.d("onPermissionRequestCanceled")
        }
    }
}
//    @JavascriptInterface
//    fun call(str: String?) {
//        Log.i("webview", "$str")
//    }

