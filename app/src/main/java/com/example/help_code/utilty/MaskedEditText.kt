package com.example.help_code.utilty

import android.content.Context
import android.text.*
import android.util.AttributeSet
import android.widget.EditText
import com.example.help_code.R
import com.example.help_code.utilty.MaskedEditText

class MaskedEditText @JvmOverloads constructor(context: Context, attr: AttributeSet?, mask: String = "", placeholder: Char = ' ') : EditText(context, attr) {
    private var mask: String
    private var placeholder: String
    private val textWatchers: MutableList<TextWatcher> = ArrayList()



    init {
        var mask = mask
        var placeholder = placeholder
        val a = context.obtainStyledAttributes(attr, R.styleable.MaskedEditText)
        val N = a.indexCount
        for (i in 0 until N) {
            val at = a.getIndex(i)
            if (at == R.styleable.MaskedEditText_mask) {
                mask = if (mask.length > 0) mask else a.getString(at)!!
            } else if (at == R.styleable.MaskedEditText_placeholder) {
                placeholder = if (a.getString(at)!!.length > 0 && placeholder == ' ') a.getString(at)!![0] else placeholder
            }
        }
        a.recycle()

        // disable text suggestions since they can influence in the mask
        inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        this.mask = mask
        this.placeholder = placeholder.toString()
        super.addTextChangedListener(MaskTextWatcher())
        if (mask.length > 0) setText(text) // sets the text to create the mask
    }

    fun getMask(): String {
        return mask
    }

    fun setMask(mask: String) {
        this.mask = mask
        text = text
    }

    fun getPlaceholder(): Char {
        return placeholder[0]
    }

    fun setPlaceholder(placeholder: Char) {
        this.placeholder = placeholder.toString()
        text = text
    }

    fun getText(removeMask: Boolean): Editable {
        return if (!removeMask) {
            text
        } else {
            val value = SpannableStringBuilder(text)
            stripMaskChars(value)
            value
        }
    }

    override fun addTextChangedListener(watcher: TextWatcher) {
        textWatchers.add(watcher)
    }

    override fun removeTextChangedListener(watcher: TextWatcher) {
        textWatchers.remove(watcher)
    }

    private fun formatMask(value: Editable) {
        val inputFilters = value.filters
        value.filters = arrayOfNulls(0)
        var i = 0
        var j = 0
        var maskLength = 0
        var treatNextCharAsLiteral = false
        val selection = Any()
        value.setSpan(selection, Selection.getSelectionStart(value), Selection.getSelectionEnd(value), Spanned.SPAN_MARK_MARK)
        while (i < mask.length) {
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
        value.filters = inputFilters
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

    private fun hasHint(): Boolean {
        val hint = hint
        return hint != null && hint.length > 0
    }

    private fun invokeBeforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        for (t in textWatchers) {
            t.beforeTextChanged(s, start, count, after)
        }
    }

    private fun invokeOnTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        for (t in textWatchers) {
            t.onTextChanged(s, start, before, count)
        }
    }

    private fun invokeAfterTextChanged(s: Editable) {
        for (t in textWatchers) {
            t.afterTextChanged(s)
        }
    }

    private inner class MaskTextWatcher : TextWatcher {
        private var updating = false
        private var originalValue: String? = null
        override fun afterTextChanged(s: Editable) {
            if (updating || mask.length == 0) return
            if (!updating) {
                updating = true
                stripMaskChars(s)
                if (s.length <= 0 && hasHint()) {
                    setText("")
                } else {
                    formatMask(s)
                }
                updating = false
                if (originalValue != getText(true).toString()) {
                    invokeAfterTextChanged(s)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            if (updating) return
            originalValue = getText(true).toString()
            invokeBeforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (updating) return
            invokeOnTextChanged(s, start, before, count)
        }
    }

    private inner class PlaceholderSpan { // this class is used just to keep track of placeholders in the text
    }

    private inner class LiteralSpan { // this class is used just to keep track of literal chars in the text
    }

    companion object {
        private const val NUMBER_MASK = '0'
        private const val ALPHA_MASK = 'A'
        private const val ALPHANUMERIC_MASK = '*'
        private const val CHARACTER_MASK = '?'
        private const val ESCAPE_CHAR = '\\'
    }
}