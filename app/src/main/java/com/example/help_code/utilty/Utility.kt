package com.example.help_code.utilty

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.help_code.BuildConfig

fun getScreenWidth(context: Context): Int {
    val displayMetrics = DisplayMetrics()
    (context as? Activity)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

//@Suppress("unused", "DEPRECATION")
//fun getSafeColor(context: Context, id: Int): Int {
//    return try {
//        ContextCompat.getColor(context, id)
//    } catch (e: Exception) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ResourcesCompat.getColor(context.resources, id, null)
//        } else {
//            context.resources.getColor(id)
//        }
//    }
//}

@Suppress("unused", "DEPRECATION")
fun Fragment.getColorSafe(id: Int): Int {
    return this.context?.getColorSafe(id) ?: throw Throwable("Context must not null")
//    return try {
//        ContextCompat.getColor(context, id)
//    } catch (e: Exception) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ResourcesCompat.getColor(context.resources, id, null)
//        } else {
//            context.resources.getColor(id)
//        }
//    }
}

@Suppress("unused", "DEPRECATION")
fun Context.getColorSafe(id: Int): Int {
    return try {
        ContextCompat.getColor(this@getColorSafe, id)
    } catch (e: Exception) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ResourcesCompat.getColor(this@getColorSafe.resources, id, null)
        } else {
            this@getColorSafe.resources.getColor(id)
        }
    }
}

fun getColorListSafe(context: Context, resId: Int): ColorStateList {
    val colorInt = context.getColorSafe(resId)
    return ColorStateList.valueOf(colorInt)
}

fun forDebugBuild(callback: () -> Unit) {
    if (BuildConfig.DEBUG) callback()
}