package com.example.help_code.presentation.webcamera

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentWebViewCameraBinding


class WebViewCameraFragment :
    BaseBindingFragment<FragmentWebViewCameraBinding>(FragmentWebViewCameraBinding::inflate) {

    @SuppressLint("JavascriptInterface")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bridge = WebViewJavascriptBridge(binding.webView)
        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.setAllowFileAccessFromFileURLs(true);
            getSettings().setAllowUniversalAccessFromFileURLs(true);

//            bridge.registerHandler("receivePhoto", object : Callback {
//                override fun handle(data: String?, responseCallback: ResponseCallback) {
//                    // Handle the received photo data here
//                    // ...
//                    val responseData = ""
//                    // Optionally send a response back to JavaScript
//                    responseCallback.call(responseData)
//                }
//            })

            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest) {
                    request.grant(request.resources)
                }
            }

            loadUrl("file:///android_asset/index.html")

        }
    }

}

interface ResponseCallback{
    fun call(responseData: String)

}
interface Callback {
    fun handle(data: String?, responseCallback: ResponseCallback)

}

class WebViewJavascriptBridge(webView: WebView) {
    fun registerHandler(s: String, any: Any) {

    }

}