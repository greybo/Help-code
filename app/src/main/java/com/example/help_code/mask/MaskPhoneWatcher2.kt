package com.example.help_code.mask

import android.text.Editable
import android.text.TextWatcher
import java.lang.Integer.min

/**
 * Text watcher allowing strictly a MASK with '#' (example: (###) ###-####
 */
abstract class MaskPhoneWatcher2(private var mask: String = "### ### ### ### ###") : TextWatcher {
    companion object {
        const val MASK_CHAR = '#'
    }

    // simple mutex
    private var isCursorRunning = false
    private var isDeleting = false

    override fun afterTextChanged(s: Editable?) {
        if (isCursorRunning || isDeleting) {
            return
        }
        isCursorRunning = true

        s?.let {
            val onlyDigits = removeMask(it.toString())
            it.clear()
            it.append(applyMask(mask, onlyDigits))
        }

        isCursorRunning = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun applyMask(mask: String, onlyDigits: String): String {
        val maskPlaceholderCharCount = mask.count { it == MASK_CHAR }
        var maskCurrentCharIndex = 0
        var output = ""

        onlyDigits.take(min(maskPlaceholderCharCount, onlyDigits.length)).forEach { c ->
            for (i in maskCurrentCharIndex until mask.length) {
                if (mask[i] == MASK_CHAR) {
                    output += c
                    maskCurrentCharIndex += 1
                    break
                } else {
                    output += mask[i]
                    maskCurrentCharIndex = i + 1
                }
            }
        }
        return output
    }

    private fun removeMask(value: String): String {
        // extract all the digits from the string
        return Regex("\\D+").replace(value, "")
    }
}
