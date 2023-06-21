package com.example.help_code

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentPhoneFormattingBinding


class PhoneFormattingFragment : BaseBindingFragment<FragmentPhoneFormattingBinding>(FragmentPhoneFormattingBinding::inflate) {

    private var textWatcherCount = 0
    private var textWatcherCount2 = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create()
    }

    private fun create() {

        binding.lblTextWatcherCount.text = "0"
        binding.txtMaskedEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                textWatcherCount++
                binding.lblTextWatcherCount.text = textWatcherCount.toString()
            }
        })

        binding.lblTextWatcherCount2.text = "0"
        binding.txtEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                textWatcherCount2++
                binding.lblTextWatcherCount2.text = textWatcherCount2.toString()
            }
        })
    }
}