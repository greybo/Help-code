package com.example.help_code.presentation.mask

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.help_code.databinding.FragmentPhoneFormattingBinding
import timber.log.Timber

//BaseBindingFragment<FragmentPhoneFormattingBinding>(FragmentPhoneFormattingBinding::inflate)
class PhoneFormattingFragment : Fragment() {

    var _binding: FragmentPhoneFormattingBinding? = null

    private var textWatcherCount = 0
    private var textWatcherCount2 = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPhoneFormattingBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        create()
    }

    private fun create() {
        val binding = _binding ?: return
        binding.lblTextWatcherCount.text = "0"
        binding.txtMaskedEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                textWatcherCount++
                binding.lblTextWatcherCount.text = textWatcherCount.toString()
            }
        })

        binding.txtEditText2.addTextChangedListener(object : MaskPhoneWatcher2() {})
        binding.txtEditText3.addTextChangedListener(MaskPhoneWatcher3())
        binding.txtEditText4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
               val formatted = getFormattedNumber(p0.toString())
                Timber.d("Formatted phone: $formatted")
            }
        })

    }
}