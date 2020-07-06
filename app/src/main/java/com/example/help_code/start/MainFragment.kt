package com.example.help_code.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.help_code.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private val route: CodeHelpRoute by lazy {
        CodeHelpRoute(
            Navigation.findNavController(requireView())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (actionBarCustom as ActionBarView).setData(ToolbarModel(title = "Help-code"))

        initAdapter()
    }

    private fun initAdapter() {
        mainRecyclerView.layoutManager = LinearLayoutManager(context)
        mainRecyclerView.adapter =
            MainAdapter(FragmentName.getList()) {
                route.navigation(it)
            }
    }
}

enum class FragmentName(val rawValue: String) {
    DropDown("DropDown");

    companion object {
        fun getList(): MutableList<String> {
            return values().map { it.rawValue }.toMutableList()
        }
    }

}