package com.example.help_code

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.Navigation
import com.example.help_code.start.ActionBarView
import com.example.help_code.base.BaseFragment
import com.example.help_code.start.MainRouter
import com.example.help_code.start.ToolbarModel
import kotlinx.android.synthetic.main.fragment_drop_down_list.*

class DropDownListFragment : BaseFragment() {

    override val route: MainRouter by lazy {
        MainRouter(
            Navigation.findNavController(
                requireView()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drop_down_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(actionBarCustom)
        initAutoComplite(requireContext(), autocompleteText, AutoCompleteList.getList()) {
            Log.i("tag_DropDown", "initAutoComplite: ${it}")
        }
    }

    override fun initToolbar(layout: ActionBarView) {
        layout.setData(
            ToolbarModel(
                homeCallback = homeToolbarCallback,
                title = "DropDownFragment"
            )
        )
    }

    private fun initAutoComplite(
        context: Context,
        view: AutoCompleteTextView,
        list: List<String>,
        callback: (String?) -> Unit
    ) {
        val adapter: ArrayAdapter<*> = ArrayAdapter(
            context,
            R.layout.item_drop_down_autocomplete, R.id.phoneTemplateText, list
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
