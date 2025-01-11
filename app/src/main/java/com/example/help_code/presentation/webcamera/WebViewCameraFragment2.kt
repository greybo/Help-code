//package com.example.help_code.presentation.webcamera
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.webkit.JavascriptInterface
//import android.webkit.WebSettings
//import com.example.help_code.base.BaseBindingFragment
//import com.example.help_code.databinding.FragmentWebViewCameraBinding
//import org.json.JSONObject
//import timber.log.Timber
//import java.io.IOException
//import java.io.InputStream
//
//
//class WebViewCameraFragment :
//    BaseBindingFragment<FragmentWebViewCameraBinding>(FragmentWebViewCameraBinding::inflate) {
//
//    @SuppressLint("JavascriptInterface")
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
////        val webSettings: WebSettings = binding.webView.settings
////        webSettings.javaScriptEnabled = true
////        binding.webView.loadUrl("https://dmytroyavorskyi.github.io")
//
////        val content = "file:///android_asset/${linkToHtml}"
////        binding.webView.loadUrl(content)
//
////        val linkToHtml = "index_main.html"
////        val htmlCode = loadHtmlFromAssets(linkToHtml)!!
////        binding.webView.loadData(htmlCode, "text/html", "UTF-8")
//
//
//        val webSettings: WebSettings = binding.webView.settings
//        webSettings.javaScriptEnabled = true
//        binding.webView.loadUrl("file:///android_asset/index_main.html");
//
//        // Підпишіться на подію onPictureTaken()
////        binding.webView.addJavascriptInterface(object : MyJavascriptInterface {
////            override fun onPictureTaken(image: String) {
////                Timber.d("image: ${image}")
////            }
////        }, "Android")
////        binding.webView.addJavascriptInterface(this, "androidObj")
//    }
//    @JavascriptInterface
//    fun call(str:String?){
//        Log.i("webview","$str")
//    }
////    @JavascriptInterface
////    fun onPictureTaken(image: String) {
////        Timber.d("image: ${image}")
////    }
////    interface MyJavascriptInterface {
////        @JavascriptInterface
////        fun onPictureTaken(image: String)
////    }
//
//    private fun loadHtmlFromAssets(fileName: String): String? {
//        return try {
//            val inputStream: InputStream = requireActivity().assets.open(fileName)
//            val size = inputStream.available()
//            val buffer = ByteArray(size)
//            inputStream.read(buffer)
//            inputStream.close()
//            String(buffer)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            ""
//        }
//    }
//}