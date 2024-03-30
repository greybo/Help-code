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
    private val viewModel3: ShopPullParseViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.parsingXML()
        val fileXml = requireContext().assets.open("products_test_file.xml")
        val listProducts = viewModel2.parseProductsXml(fileXml)
        listProducts.map {
            Timber.d(it.toString())
        }
        val fileXmlShop = requireContext().assets.open("sales_drive_db.xml")
        val listProducts2 = viewModel3.parseShopXml(fileXmlShop)
//        listProducts2.categories.map {
//            Timber.d("shop categories: ${ it.toString() }")
//        }
        Timber.d("shop offers size: ${ listProducts2?.offers?.size }")
//        listProducts2.offers.map {
//            Timber.d("shop offers: ${ it.toString() }")
//        }
    }
}