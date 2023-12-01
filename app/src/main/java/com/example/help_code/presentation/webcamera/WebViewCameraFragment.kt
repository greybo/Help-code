//package com.example.help_code.presentation.webcamera
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.util.Log
//import android.view.View
//import android.webkit.ValueCallback
//import android.webkit.WebChromeClient
//import android.webkit.WebView
//import com.example.help_code.base.BaseBindingFragment
//import com.example.help_code.databinding.FragmentWebViewCameraBinding
//import timber.log.Timber
//import java.io.File
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.*
//
//class WebViewCameraFragment : BaseBindingFragment<FragmentWebViewCameraBinding>(FragmentWebViewCameraBinding::inflate) {
////    private val webView: WebView? = null
//    private val mActivity: Context? = null
//    private var mUploadMessage: ValueCallback<Uri?>? = null
//    private var mUploadMessageForAndroid5: ValueCallback<Array<Uri>>? = null
//    private var mCurrentPhotoPath: String? = null
//    var items: Array<CharSequence> = emptyArray()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
////        binding.webView.webChromeClient = object : WebChromeClient() {
////            override fun onShowFileChooser(
////                webView: WebView?,
////                filePathCallback: ValueCallback<Array<Uri>>?,
////                fileChooserParams: FileChooserParams?
////            ): Boolean {
////                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
////            }
////        }
//        //JS
//        binding.webView.settings.javaScriptEnabled = true
//
////set ChromeClient
//        binding.webView.webChromeClient = chromeClient
////        mUploadMessage.onReceiveValue()
//        "https://dmytroyavorskyi.github.io/"
//        binding.webView.setWebViewClient(android.webkit.WebViewClient())
//        binding. webView.loadUrl("https://dmytroyavorskyi.github.io/")
//    }
//
//
//
//    private val chromeClient: WebChromeClient
//        //ChromeClinet配置
//        private get() = object : WebChromeClient() {
//            //3.0++
//            fun openFileChooser(uploadMsg: ValueCallback<Uri?>, acceptType: String?) {
//                openFileChooserImpl(uploadMsg)
//            }
//
//            //3.0--
//            fun openFileChooser(uploadMsg: ValueCallback<Uri?>) {
//                openFileChooserImpl(uploadMsg)
//            }
//
//            fun openFileChooser(uploadMsg: ValueCallback<Uri?>, acceptType: String?, capture: String?) {
//                openFileChooserImpl(uploadMsg)
//            }
//
//            // For Android > 5.0
//            override fun onShowFileChooser(webView: WebView, uploadMsg: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams): Boolean {
//                openFileChooserImplForAndroid5(uploadMsg)
//                return true
//            }
//        }
//
//    private fun openFileChooserImpl(uploadMsg: ValueCallback<Uri?>) {
//        mUploadMessage = uploadMsg
//        AlertDialog.Builder(mActivity)
//            .setItems(items as Array<CharSequence>) { dialog: DialogInterface, which: Int ->
//                if (items[which] == items[0]) {
//                    val i = Intent(Intent.ACTION_GET_CONTENT)
//                    i.addCategory(Intent.CATEGORY_OPENABLE)
//                    i.type = "image/*"
//                    startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)
//                } else if (items[which] == items[1]) {
//                    dispatchTakePictureIntent()
//                }
//                dialog.dismiss()
//            }
//            .setOnCancelListener { dialog ->
//                Log.v("TAG", "TAG" + " # onCancel")
//                mUploadMessage = null
//                dialog.dismiss()
//            }
//            .show()
//    }
//
//    private fun openFileChooserImplForAndroid5(uploadMsg: ValueCallback<Array<Uri>>) {
//        mUploadMessageForAndroid5 = uploadMsg
//        AlertDialog.Builder(mActivity)
//            .setItems(items as Array<CharSequence>) { dialog: DialogInterface, which: Int ->
//                if (items[which] == items[0]) {
//                    val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
//                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
//                    contentSelectionIntent.type = "image/*"
//                    val chooserIntent = Intent(Intent.ACTION_CHOOSER)
//                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
//                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
//                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5)
//                } else if (items[which] == items[1]) {
//                    dispatchTakePictureIntent()
//                }
//                dialog.dismiss()
//            }
//            .setOnCancelListener { dialog ->
//                Log.v(TAG, TAG + " # onCancel")
//                //important to return new Uri[]{}, when nothing to do. This can slove input file wrok for once.
//                //InputEventReceiver: Attempted to finish an input event but the input event receiver has already been disposed.
//                mUploadMessageForAndroid5!!.onReceiveValue(arrayOf())
//                mUploadMessageForAndroid5 = null
//                dialog.dismiss()
//            }.show()
//    }
//
//    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(mActivity!!.packageManager) != null) {
////            File file = new File(createImageFile());
//            var imageUri: Uri? = null
//            try {
//                imageUri = Uri.fromFile(createImageFile())
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            //temp sd card file
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//            startActivityForResult(takePictureIntent, REQUEST_GET_THE_THUMBNAIL)
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageFileName = "JPEG_" + timeStamp + "_"
//        val storageDir = File(Environment.getExternalStorageDirectory().absolutePath + "/don_test/")
//        if (!storageDir.exists()) {
//            storageDir.mkdirs()
//        }
//        val image = File.createTempFile(
//            imageFileName,  /* prefix */
//            ".jpg",  /* suffix */
//            storageDir /* directory */
//        )
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.absolutePath
//        return image
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//        Timber.tag(TAG).v("$TAG # onActivityResult # requestCode=$requestCode # resultCode=$resultCode")
//        if (requestCode == FILECHOOSER_RESULTCODE) {
//            if (null == mUploadMessage) return
//            val result = if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
//            mUploadMessage!!.onReceiveValue(result)
//            mUploadMessage = null
//        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
//            if (null == mUploadMessageForAndroid5) return
//            val result: Uri?
//            result = if (intent == null || resultCode != Activity.RESULT_OK) {
//                null
//            } else {
//                intent.data
//            }
//            if (result != null) {
//                Log.v(TAG, TAG + " # result.getPath()=" + result.path)
//                mUploadMessageForAndroid5!!.onReceiveValue(arrayOf(result))
//            } else {
//                mUploadMessageForAndroid5!!.onReceiveValue(arrayOf())
//            }
//            mUploadMessageForAndroid5 = null
//        } else if (requestCode == REQUEST_GET_THE_THUMBNAIL) {
//            if (resultCode == Activity.RESULT_OK) {
//                val file = File(mCurrentPhotoPath)
//                val localUri = Uri.fromFile(file)
//                val localIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri)
//                mActivity!!.sendBroadcast(localIntent)
//                val result = Uri.fromFile(file)
//                mUploadMessageForAndroid5!!.onReceiveValue(arrayOf(result))
//                mUploadMessageForAndroid5 = null
//            } else {
//                val file = File(mCurrentPhotoPath)
//                Log.v(TAG, TAG + " # file=" + file.exists())
//                if (file.exists()) {
//                    file.delete()
//                }
//            }
//        }
//    }
//
//    companion object {
//        var TAG = "Log_TAG"
//        private const val REQUEST_GET_THE_THUMBNAIL = 4000
//        private const val ANIMATION_DURATION: Long = 200
//        const val FILECHOOSER_RESULTCODE = 1
//        const val FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2
//    }
//
//}