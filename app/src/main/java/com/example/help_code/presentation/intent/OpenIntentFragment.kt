package com.example.help_code.presentation.intent

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.help_code.BuildConfig
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentOpenIntentBinding

class OpenIntentFragment :  BaseBindingFragment<FragmentOpenIntentBinding>(FragmentOpenIntentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareApp(requireContext())
    }
    fun shareApp(context: Context) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://crownsydney.com.au"))
        startActivity(browserIntent)
    }

//    @SuppressLint("QueryPermissionsNeeded")
    fun Fragment.openUrlExceptCurrentApp(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val packageManager = requireActivity().packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)
        val packageNameToHide = BuildConfig.APPLICATION_ID
        val targetIntents = ArrayList<Intent>()
        for (currentInfo in activities) {
            val packageName = currentInfo.activityInfo.packageName
            if (packageNameToHide != packageName) {
                val targetIntent = Intent(Intent.ACTION_VIEW)
                targetIntent.setData(Uri.parse(url))
                targetIntent.setPackage(packageName)
                targetIntents.add(targetIntent)
            }
        }
        if (targetIntents.size > 0) {
            val chooserIntent = Intent.createChooser(targetIntents.removeAt(0), "Open with")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(arrayOf<Parcelable>()))
            startActivity(chooserIntent)
        } else {
            Toast.makeText(requireContext(), "No app found", Toast.LENGTH_SHORT).show()
        }
    }
}