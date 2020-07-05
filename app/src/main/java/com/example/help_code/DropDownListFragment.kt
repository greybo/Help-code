package com.example.help_code

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import kotlinx.android.synthetic.main.fragment_drop_down_list.*

class DropDownListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drop_down_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAutoComplite(requireContext(),autocompleteText,getTemplateList()){
            Log.i("tag_DropDown", "initAutoComplite: ${it}")
        }
    }

    private fun initAutoComplite(
        context: Context,
        view: AutoCompleteTextView,
        list: List<String>,
        callback: (String?) -> Unit
    ) {
        var check = 0
        view.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i("face_tag_template", "onNothingSelected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (check++ > 0) {
                    val template = list.getOrNull(position)
                    callback(template)
                    Log.i("face_tag_template", "template: ${template}")
                }
            }
        }
        val adapter: ArrayAdapter<*> = ArrayAdapter(
            context,
            R.layout.item_drop_down_autocomplete, R.id.phoneTemplateText, list
        )

        view.setAdapter(adapter)
    }

    private fun getTemplateList(): List<String> {
        return PhoneTemplate.values().map { it.rawValue }
    }

    enum class PhoneTemplate(val rawValue: String) {
        Australia("+61 (Australia)"),
        NewZealand("+63 (New Zealand)")
    }
}
