package com.example.help_code.presentation.inputtext

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.Navigation
import com.example.help_code.MainRouter
import com.example.help_code.R
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentInputTextListBinding

class InputTextViewFragment :
    BaseBindingFragment<FragmentInputTextListBinding>(FragmentInputTextListBinding::inflate) {

    private val route: MainRouter by lazy {
        MainRouter(
            Navigation.findNavController(
                requireView()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainToolbar.getBuilder()
            .title(title = "DropDownFragment")
            .homeCallback {
                route.onBackPress()
            }
            .create()
        initAutoComplite(requireContext(), binding.autocompleteText, AutoCompleteList.getList()) {
            Log.i("tag_DropDown", "initAutoComplite: ${it}")
        }
    }

    private fun initAutoComplite(
        context: Context,
        view: AutoCompleteTextView,
        list: List<String>,
        callback: (String?) -> Unit
    ) {
        val adapter: ArrayAdapter<*> = ArrayAdapter(
            context,
            R.layout.item_input_autocomplete, R.id.phoneTemplateText, list
        )
        view.setOnItemClickListener { _, _, position, id ->
            val template = list.getOrNull(position)
            callback(template)
            Log.i("face_tag_template", "template: ${template}")
        }
        view.setDropDownBackgroundResource(R.drawable.bg_button_default)
        view.setAdapter(adapter)
    }


    enum class AutoCompleteList(val rawValue: String) {
        Australia("+61 (Australia)"),
        NewZealand("+63 (New Zealand)");

        companion object {
            fun getList(): List<String> {
                return values().map { it.rawValue }
            }
        }
    }
}
