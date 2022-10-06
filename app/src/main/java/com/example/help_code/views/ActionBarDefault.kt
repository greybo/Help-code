package com.example.help_code.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.example.help_code.R
import kotlinx.android.synthetic.main.action_bar_custom.view.*

class ActionBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.action_bar_custom, this)
    }

    fun setData(model: ActionBarModel) {
        model.homeCallback?.let {
            toolbar.setNavigationIcon(ArrowId.Black.id)
            toolbar.setNavigationOnClickListener {
                it()
            }
        }
        model.title?.let { toolbar_custom_title.text = it }
    }
}

data class ActionBarModel(
    val homeCallback: (() -> Unit)? = null,
    val title: String? = null
)

enum class ArrowId(val id: Int) {
    Black(R.drawable.ic_arrow_back_24dp), White(R.drawable.ic_arrow_white_24dp)
}