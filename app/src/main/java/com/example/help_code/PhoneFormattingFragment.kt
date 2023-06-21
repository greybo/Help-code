package com.example.help_code

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentPhoneFormattingBinding
import com.example.help_code.utilty.MaskedEditText
import java.util.*


class PhoneFormattingFragment : BaseBindingFragment<FragmentPhoneFormattingBinding>(FragmentPhoneFormattingBinding::inflate) {

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_phone_formatting, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.phoneNumberField.addTextChangedListener(object : TextWatcher {
//            private var mFormatting = false// a flag that prevents stack overflows.
//
//            private var mAfter = 0
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//
//            //called before the text is changed...
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                mAfter = after // flag to detect backspace.
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                // Make sure to ignore calls to afterTextChanged caused by the work done below
//                if (!mFormatting) {
//                    mFormatting = true
//                    // using US formatting.
//                    if (mAfter != 0) // in case back space ain't clicked.
//                        PhoneNumberUtils.formatNumber(
//                            s, PhoneNumberUtils.getFormatTypeForLocale(Locale.US)
//                        )
//                    mFormatting = false
//                }
//            }
//        })

        onCreate()
    }


    private var textWatcherCount = 0
    private var textWatcherCount2 = 0
//    private var lblTextWatcherCount: TextView? = null
//    private var txtMaskedEditText: MaskedEditText? = null
//    private var lblTextWatcherCount2: TextView? = null
//    private var txtEditText: EditText? = null

    fun onCreate() {


//        lblTextWatcherCount = binding.lblTextWatcherCount
        binding.lblTextWatcherCount.text = "0"
//        txtMaskedEditText = binding.txtMaskedEditText
        binding.txtMaskedEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                textWatcherCount++
                binding.lblTextWatcherCount.text = textWatcherCount.toString()
            }
        })
//        lblTextWatcherCount2 = binding.lblTextWatcherCount2
        binding.lblTextWatcherCount2.text = "0"
//        txtEditText = binding.txtEditText
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