package com.example.help_code.presentation.blank

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlankBinding

class BlankFragment : BaseBindingFragment<FragmentBlankBinding>(FragmentBlankBinding::inflate) {

    private val viewModel: BlankViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.blankFragmentButton.setOnClickListener {
            requireActivity().run {
                startActivity(intentSettingsApp)
            }
        }
    }

}

val FragmentActivity.intentSettingsApp: Intent
    get() = this.run {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {//ACTION_LOCATION_SOURCE_SETTINGS
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package:$packageName")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        }
    }

//putExtra(
//   Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//   BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL
//)