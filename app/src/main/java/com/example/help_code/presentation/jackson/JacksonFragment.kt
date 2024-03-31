package com.example.help_code.presentation.jackson

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentJacksonTestBinding
import timber.log.Timber

class JacksonFragment :
    BaseBindingFragment<FragmentJacksonTestBinding>(FragmentJacksonTestBinding::inflate) {
    private val viewModel: JacksonMapperViewModel by viewModels()
    private val viewModel2: PullParserViewModel by viewModels()
    private val viewModel3: ShopViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.parsingXML()
//        val fileXml = requireContext().assets.open("products_test_file.xml")
//        val listProducts = viewModel2.parseProductsXml(fileXml)
//        listProducts.map {
//            Timber.d(it.toString())
//        }
        val fileXmlShop = requireContext().assets.open("sales_drive_db.xml")
        val listProducts2 = viewModel3.fetchData(fileXmlShop)

        listProducts2?.map { category ->
            Timber.d("categories: name: ${category.name}, id: ${category.id}")
            Timber.d("subCategories size: ${category.subCategories.size}")
            category.subCategories.map { category2 ->
                Timber.d("  categories2: name: ${category2.name}, id: ${category2.id}")
                Timber.d("  subCategories2 size: ${category2.subCategories.size}")
//                Timber.d("  subCategories2: ${category2.subCategories}")
                category2.subCategories.map { category3 ->
                    Timber.d("    categories3: name: ${category3.name}, id: ${category3.id}")
                    Timber.d("    subCategories3 size: ${category3.subCategories.size}")
//                    Timber.d("    subCategories3: ${category3.subCategories}")

                }
            }
            Timber.d("==================================")
        }
    }
}