package au.com.crownresorts.crma.feature.signup.data.creater

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT
import android.util.Base64
import android.util.Base64OutputStream
import java.io.ByteArrayOutputStream
import java.io.File

class PhotoUtil() {

    val imageFile: File? = File("")
    val bitmap: Bitmap? = BitmapFactory.decodeFile("")
    val imageBase64: String
        get() = imageFile?.let {
            convertImageFileToBase64(it)
        } ?: "No selfie image!"
    val compressedDocBackImageBase64: String?
        get() = bitmap?.let {
            compressImageFileToBase64(it)
        }

    private fun convertImageFileToBase64(imageFile: File): String {
        return ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                imageFile.inputStream().use { inputStream ->
                    inputStream.copyTo(base64FilterStream)
                }
            }
            return@use outputStream.toString()
        }
    }

    private fun compressImageFileToBase64(bitmap: Bitmap): String {
        return ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, base64FilterStream)
            }
            return@use outputStream.toString()
        }
    }

    private fun getCroppedImage(bitmap: Bitmap): String {
        return ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                getCroppedSelfieBitmap(bitmap).compress(Bitmap.CompressFormat.JPEG, 100, base64FilterStream)
            }
            return@use outputStream.toString()
        }
    }

    private fun getCroppedSelfieBitmap(input: Bitmap): Bitmap {
        return ThumbnailUtils.extractThumbnail(
            input,
            720,
            960,
            OPTIONS_RECYCLE_INPUT
        )
    }
}