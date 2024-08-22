package com.example.help_code.presentation.blank_test

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlank2Binding
import com.example.help_code.presentation.xlsx.ToExelModel
import com.example.help_code.presentation.xlsx.writeToExcel


class BlankFragment : BaseBindingFragment<FragmentBlank2Binding>(FragmentBlank2Binding::inflate) {


    private val viewModel: BlankViewModel by viewModels()

    //    private val exelFile = WriteToExcelFile()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val current = resources.configuration.locale
//        val country = current?.country
//        toastDebug("country: $country")

        requireContext().writeToExcel(arrayOf(
            ToExelModel("1","address 1","name 1",23),
            ToExelModel("2","address 2","name 2",676),
            ToExelModel("3","address 3","name 3",33),
            ToExelModel("4","address 4","name 4",345),
        ))

//        requireContext().jsonToExcelFetch(
//        arrayOf(
//            ToExelModel("1","address 1","name 1",23),
//            ToExelModel("2","address 2","name 2",676),
//            ToExelModel("3","address 3","name 3",33),
//            ToExelModel("4","address 4","name 4",345),
//        )
//        )
    }
}