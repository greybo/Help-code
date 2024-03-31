package com.example.help_code.presentation.jackson

import androidx.lifecycle.ViewModel
import java.io.InputStream

class ShopViewModel : ViewModel() {
    var shopModel: Shop? = null
    fun fetchData(input: InputStream): List<Category>? {
        shopModel = ShopPullParse.parse(input)
        return shopModel?.let { buildCategoriesHierarchy(it.categories) }
    }

    private fun buildCategoriesHierarchy(categories: List<Category>): List<Category> {
        val categoriesMap = categories.associateBy { it.id }
        val rootCategories = mutableListOf<Category>()

        categories.forEach { category ->
            if (category.parentId != null) {
                categoriesMap[category.parentId]?.subCategories?.add(category)
            } else {
                rootCategories.add(category)
            }
        }

        return rootCategories
    }
}

