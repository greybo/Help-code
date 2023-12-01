package com.example.help_code.presentation.webcamera

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentWebViewCameraBinding
import java.io.IOException
import java.io.InputStream


class WebViewCameraFragment : BaseBindingFragment<FragmentWebViewCameraBinding>(FragmentWebViewCameraBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //JS
//        binding.webView.settings.javaScriptEnabled = true
//        mUploadMessage.onReceiveValue()
//        binding.webView.setWebViewClient(android.webkit.WebViewClient())
//        binding.webView.loadUrl("https://dmytroyavorskyi.github.io")

//        val content = "file:///android_asset/${linkToHtml}"
//        binding.webView.loadUrl(content)

//        val linkToHtml = "camera_page.html"
//        val htmlCode = loadHtmlFromAssets(linkToHtml)!!
//        binding.webView.loadData(htmlCode, "text/html", "UTF-8")



        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        binding.webView.loadUrl("file:///android_asset/camera_page.html");

        // Підпишіться на подію onPictureTaken()
        binding.webView.addJavascriptInterface(object : JavascriptInterface {
            override fun onPictureTaken(image: String) {
                // Обробка зображення
            }
        }, "Android")
    }
   interface JavascriptInterface {

        @JavascriptInterface
        open fun onPictureTaken(image: String) {
            // Обробка зображення
        }

    }
    private fun loadHtmlFromAssets(fileName: String): String? {
        return try {
            val inputStream: InputStream = requireActivity().assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}