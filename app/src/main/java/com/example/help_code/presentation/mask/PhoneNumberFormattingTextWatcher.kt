//package com.example.help_code.presentation.mask
//
//import android.telephony.PhoneNumberUtils
//import android.text.Editable
//import android.text.Selection
//import android.text.TextWatcher
//import android.util.Log
//import java.util.*
//import com.google.i18n.phonenumbers.AsYouTypeFormatter
//import com.google.i18n.phonenumbers.PhoneNumberUtil
///**
// * Watches a [android.widget.TextView] and if a phone number is entered
// * will format it.
// *
// *
// * Stop formatting when the user
// *
// *  * Inputs non-dialable characters
// *  * Removes the separator in the middle of string.
// *
// *
// *
// * The formatting will be restarted once the text is cleared.
// */
//class PhoneNumberFormattingTextWatcher @JvmOverloads constructor(countryCode: String? = Locale.getDefault().getCountry()) : TextWatcher {
//    /**
//     * Indicates the change was caused by ourselves.
//     */
//    private var mSelfChange = false
//
//    /**
//     * Indicates the formatting has been stopped.
//     */
//    private var mStopFormatting = false
//    private val mFormatter: AsYouTypeFormatter
//    private val countryCode: String
//    /**
//     * The formatting is based on the given `countryCode`.
//     *
//     * @param countryCode the ISO 3166-1 two-letter country code that indicates the country/region
//     * where the phone number is being entered.
//     *
//     * @hide
//     */
//    /**
//     * The formatting is based on the current system locale and future locale changes
//     * may not take effect on this instance.
//     */
//    init {
//        requireNotNull(countryCode)
//        mFormatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter(countryCode)
//        this.countryCode = countryCode
//    }
//
//    override fun beforeTextChanged(
//        s: CharSequence, start: Int, count: Int,
//        after: Int
//    ) {
//        if (mSelfChange || mStopFormatting) {
//            return
//        }
//        // If the user manually deleted any non-dialable characters, stop formatting
//        if (count > 0 && hasSeparator(s, start, count)) {
//            stopFormatting()
//        }
//    }
//
//    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//        if (mSelfChange || mStopFormatting) {
//            return
//        }
//        // If the user inserted any non-dialable characters, stop formatting
//        if (count > 0 && hasSeparator(s, start, count)) {
//            stopFormatting()
//        }
//    }
//
//    @Synchronized
//    override fun afterTextChanged(s: Editable) {
//        if (mStopFormatting) {
//            // Restart the formatting when all texts were clear.
//            mStopFormatting = s.isNotEmpty()
//            return
//        }
//        if (mSelfChange) {
//            // Ignore the change caused by s.replace().
//            return
//        }
//        val formatted = reformat(s, Selection.getSelectionEnd(s))
//        if (formatted != null) {
//            val rememberedPos = formatted.length
//            Log.v("rememberedPos", "" + rememberedPos)
//            mSelfChange = true
//            s.replace(0, s.length, formatted, 0, formatted.length)
//
//
//            // The text could be changed by other TextWatcher after we changed it. If we found the
//            // text is not the one we were expecting, just give up calling setSelection().
//            if (formatted == s.toString()) {
//                Selection.setSelection(s, rememberedPos)
//            }
//            mSelfChange = false
//        }
//    }
//
//    /**
//     * Generate the formatted number by ignoring all non-dialable chars and stick the cursor to the
//     * nearest dialable char to the left. For instance, if the number is  (650) 123-45678 and '4' is
//     * removed then the cursor should be behind '3' instead of '-'.
//     */
//    private fun reformat(s: CharSequence, cursor: Int): String {
//        // The index of char to the leftward of the cursor.
//        var s = s
//        val curIndex = cursor - 1
//        var formatted: String? = null
//        mFormatter.clear()
//        var lastNonSeparator = 0.toChar()
//        var hasCursor = false
//        val countryCallingCode = "+" + "61"//CountryCodesAdapter.getCode(countryCode)
//        s = countryCallingCode + s
//        val len = s.length
//        for (i in 0 until len) {
//            val c = s[i]
//            if (PhoneNumberUtils.isNonSeparator(c)) {
//                if (lastNonSeparator.code != 0) {
//                    formatted = getFormattedNumber(lastNonSeparator, hasCursor)
//                    hasCursor = false
//                }
//                lastNonSeparator = c
//            }
//            if (i == curIndex) {
//                hasCursor = true
//            }
//        }
//        if (lastNonSeparator.code != 0) {
//            Log.v("lastNonSeparator", "" + lastNonSeparator)
//            formatted = getFormattedNumber(lastNonSeparator, hasCursor)
//        }
//        return if (formatted!!.length > countryCallingCode.length) {
//            if (formatted[countryCallingCode.length] == ' ') formatted.substring(countryCallingCode.length + 1) else formatted.substring(
//                countryCallingCode.length
//            )
//        } else formatted.substring(formatted.length)
//    }
//
//    private fun getFormattedNumber(lastNonSeparator: Char, hasCursor: Boolean): String {
//        return if (hasCursor) mFormatter.inputDigitAndRememberPosition(lastNonSeparator) else mFormatter.inputDigit(lastNonSeparator)
//    }
//
//    private fun stopFormatting() {
//        mStopFormatting = true
//        mFormatter.clear()
//    }
//
//    private fun hasSeparator(s: CharSequence, start: Int, count: Int): Boolean {
//        for (i in start until start + count) {
//            val c = s[i]
//            if (!PhoneNumberUtils.isNonSeparator(c)) {
//                return true
//            }
//        }
//        return false
//    }
//}