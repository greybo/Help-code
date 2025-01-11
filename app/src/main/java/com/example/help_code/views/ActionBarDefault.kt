package com.example.help_code.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.help_code.R
import com.example.help_code.databinding.ActionBarCustomBinding
import com.example.help_code.utilty.inflateAdapter

class ActionBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var binding = inflateAdapter(ActionBarCustomBinding::inflate)

    fun setData(model: ActionBarModel) {
        model.homeCallback?.let {
            binding.toolbar.setNavigationIcon(ArrowId.Black.id)
            binding.toolbar.setNavigationOnClickListener {
                it()
            }
        }
        model.title?.let { binding.toolbarCustomTitle.text = it }
    }
}

data class ActionBarModel(
    val homeCallback: (() -> Unit)? = null,
    val title: String? = null
)

enum class ArrowId(val id: Int) {
    Black(R.drawable.ic_arrow_back_24dp), White(R.drawable.ic_arrow_white_24dp)
}