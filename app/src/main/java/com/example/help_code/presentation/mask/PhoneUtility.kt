package com.example.help_code.presentation.mask

import androidx.fragment.app.Fragment
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonemetadata
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.util.*


class PhoneUtility {

}

fun Fragment.getFormattedNumber(phoneNumber: String): String? {
    var phoneNumberHandled: String? = phoneNumber
    val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(requireContext())//getInstance()
    val numberFormat: Phonemetadata.NumberFormat = Phonemetadata.NumberFormat()
//    numberFormat.pattern = "(\\d{3})(\\d{3})(\\d{4})"
    numberFormat.pattern = "(\\d{2})(\\d{2})(\\d{2})"
    numberFormat.format = "($1) $2-$3"
    val newNumberFormats: MutableList<Phonemetadata.NumberFormat> = ArrayList<Phonemetadata.NumberFormat>()
    newNumberFormats.add(numberFormat)
    var phoneNumberPN: Phonenumber.PhoneNumber? = null
    try {
        phoneNumberPN = phoneNumberUtil.parse(phoneNumber, Locale.getDefault().country)
        phoneNumberHandled = phoneNumberUtil.formatByPattern(phoneNumberPN, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL, newNumberFormats)
    } catch (e: NumberParseException) {
        e.printStackTrace()
    }
    return phoneNumberHandled
}