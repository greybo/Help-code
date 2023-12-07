package com.example.help_code.presentation.webcamera

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
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
            loadUrl("file:///android_asset/index.html")

//            bridge.registerHandler("receivePhoto", object : Callback {
//                override fun handle(data: String?, responseCallback: ResponseCallback) {
//                    // Handle the received photo data here
//                    // ...
//                    val responseData = ""
//                    // Optionally send a response back to JavaScript
//                    responseCallback.call(responseData)
//                }
//            })
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