package com.example.help_code.mask

import android.text.Editable
import android.text.Selection
import android.text.Spanned
import android.text.TextWatcher

class MaskPhoneWatcher3 : TextWatcher {

    private var mask: String = "000 000 000 000 000 000"
    private var placeholder: String = " "
    private var updating = false

    init {


    }

    override fun afterTextChanged(s: Editable) {
        if (mask.isEmpty()) return
        if (!updating) {
            updating = true
            stripMaskChars(s)
            if (s.isEmpty()) {
                s.clear()
            } else {
                formatMask(s)
            }
            updating = false
        }

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    private fun formatMask(value: Editable) {
        value.filters = arrayOfNulls(0)
        var i = 0
        var j = 0
        var maskLength = 0
        var treatNextCharAsLiteral = false
        val selection = Any()
        value.setSpan(selection, Selection.getSelectionStart(value), Selection.getSelectionEnd(value), Spanned.SPAN_MARK_MARK)
        while (i < value.length && i < mask.length) {
            if (!treatNextCharAsLiteral && isMaskChar(mask[i])) {
                if (j >= value.length) {
                    value.insert(j, placeholder)
                    value.setSpan(PlaceholderSpan(), j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    j++
                } else if (!matchMask(mask[i], value[j])) {
                    value.delete(j, j + 1)
                    i--
                    maskLength--
                } else {
                    j++
                }
                maskLength++
            } else if (!treatNextCharAsLiteral && mask[i] == ESCAPE_CHAR) {
                treatNextCharAsLiteral = true
            } else {
                value.insert(j, mask[i].toString())
                value.setSpan(LiteralSpan(), j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                treatNextCharAsLiteral = false
                j++
                maskLength++
            }
            i++
        }
        while (value.length > maskLength) {
            val pos = value.length - 1
            value.delete(pos, pos + 1)
        }
        Selection.setSelection(value, value.getSpanStart(selection), value.getSpanEnd(selection))
        value.removeSpan(selection)
    }

    private fun stripMaskChars(value: Editable) {
        val pSpans = value.getSpans(0, value.length, PlaceholderSpan::class.java)
        val lSpans = value.getSpans(0, value.length, LiteralSpan::class.java)
        for (pSpan in pSpans) {
            value.delete(value.getSpanStart(pSpan), value.getSpanEnd(pSpan))
        }
        for (lSpan in lSpans) {
            value.delete(value.getSpanStart(lSpan), value.getSpanEnd(lSpan))
        }
    }

    private fun matchMask(mask: Char, value: Char): Boolean {
        var ret = mask == NUMBER_MASK && Character.isDigit(value)
        ret = ret || mask == ALPHA_MASK && Character.isLetter(value)
        ret = ret || mask == ALPHANUMERIC_MASK && (Character.isDigit(value) || Character.isLetter(value))
        ret = ret || mask == CHARACTER_MASK
        return ret
    }

    private fun isMaskChar(mask: Char): Boolean {
        when (mask) {
            NUMBER_MASK, ALPHA_MASK, ALPHANUMERIC_MASK, CHARACTER_MASK -> return true
        }
        return false
    }

    private inner class PlaceholderSpan // this class is used just to keep track of placeholders in the text


    private inner class LiteralSpan  // this class is used just to keep track of literal chars in the text


    companion object {
        private const val NUMBER_MASK = '0'
        private const val ALPHA_MASK = 'A'
        private const val ALPHANUMERIC_MASK = '*'
        private const val CHARACTER_MASK = '?'
        private const val ESCAPE_CHAR = '\\'
    }
}