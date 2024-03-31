package com.example.help_code.presentation.jackson

import kotlinx.coroutines.coroutineScope
import timber.log.Timber


class ShopProvider(private val shop: Shop?) {

    private val offerGroups by lazy { getOffers()?.groupBy { it.categoryId } }
    val categoryGroupId by lazy { getCategories()?.groupBy { it.id } }
    val categoryGroupParenId by lazy { getCategories()?.groupBy { it.parentId ?: 0 } }
    val categoryHead by lazy { categoryGroupParenId?.get(0) }
    var rootCategories: List<Category>? = null
    val secondListCategories = listOf(
        100698824,
        61445709,
        66645614,
        85398348,
        85398465
    )

    suspend fun buildAsFolder(level: Int = 0): List<List<Int>> {
        if (rootCategories == null) {
            rootCategories = getCategories()?.let {
                buildCategoriesHierarchy(it)
            }
        }
        val root = rootCategories ?: return emptyList()


        // Припускаючи, що у вас є список кореневих категорій `rootCategories`
        val allPaths = mutableListOf<String>()
        val allPathsId = mutableListOf<List<Int>>()
        root.forEach { rootCategory ->
            buildPathIDsForCategories(rootCategory, emptyList(), allPathsId)
        }
        allPathsId.map {
            Timber.d(it.joinToString("/"))
        }.run {
            Timber.d("path list size: ${this.size}")
        }

        val filterList = allPathsId.mapNotNull { it.getOrNull(level) }.run {
            getCategories()?.filter { it.id in this }
        } ?: emptyList()

        val countsList = getCounts(filterList)
        Timber.d("ProductCount size: ${countsList.size}")
        countsList.map {
            Timber.d(" ${it.id},  ${it.count},")
        }

        return allPathsId
    }

    fun buildPathIDsForCategories(
        category: Category,
        currentPath: List<Int> = emptyList(),
        pathIds: MutableList<List<Int>> = mutableListOf()
    ): List<List<Int>> {
        // Додаємо назву поточної категорії до шляху
        val newPath: List<Int> =
            if (currentPath.isEmpty()) listOf(category.id)
            else currentPath.plus(category.id)

        // Якщо у категорії немає підкатегорій, зберігаємо шлях
        if (category.subCategories.isEmpty()) {
            pathIds.add(newPath)
        } else {
            // Рекурсивно викликаємо функцію для всіх підкатегорій
            category.subCategories.forEach { subCategory ->
                buildPathIDsForCategories(subCategory, newPath, pathIds)
            }
        }

        return pathIds
    }

    fun getCounts(rootCategories: List<Category>): List<ProductCount> {
        val countsList = mutableListOf<ProductCount>()
        rootCategories.forEach { rootCategory ->
            calculateCountForRoot(rootCategory.id, rootCategory, 0, countsList)
        }

        return calculateByGroup(countsList)
    }

    private fun calculateByGroup(list: MutableList<ProductCount>): List<ProductCount> {
        return list.groupBy { it.id }.entries.map { entry ->
            val count = entry.value.map { it.count }.reduce { acc, count ->
                acc + count
            }
            ProductCount(entry.key, count)
        }
    }

    private fun calculateCountForRoot(
        parentId: Int,
        category: Category,
        currentCount: Int = 0,
        counts: MutableList<ProductCount> = mutableListOf()
    ): List<ProductCount> {

        val newCount = currentCount + category.countInner
        if (category.subCategories.isEmpty()) {
            counts.add(ProductCount(parentId, newCount))
        } else {
            category.subCategories.forEach { subCategory ->
                calculateCountForRoot(parentId, subCategory, newCount, counts)
            }
        }
        return counts
    }


    suspend fun buildCategoriesHierarchy(categories: List<Category>): List<Category> {
        return coroutineScope {
            val categoriesMap = categories.associateBy { it.id }
            val rootCategories = mutableListOf<Category>()

            categories.forEach { category ->
                category.countInner = offerGroups?.get(category.id)?.size ?: 0
                if (category.parentId != null) {
                    categoriesMap[category.parentId]?.subCategories?.add(category)
                } else {
                    rootCategories.add(category)
                }
            }
            rootCategories
        }
    }

    val offerSize get() = getOffers()?.size ?: 0
    fun getCategories(): List<Category>? {
        return shop?.categories
    }

    fun getOffers(): List<Offer>? {
        return shop?.offers
    }

}

data class ProductCount(val id: Int, val count: Int)


//    private fun buildPathsForCategories(
//        category: Category,
//        currentPath: String = "",
//        paths: MutableList<String> = mutableListOf()
//    ): List<String> {
//        // Додаємо назву поточної категорії до шляху
//        val newPath = if (currentPath.isEmpty()) category.name else "$currentPath\\${category.name}"
//
//        // Якщо у категорії немає підкатегорій, зберігаємо шлях
//        if (category.subCategories.isEmpty()) {
//            paths.add(newPath)
//        } else {
//            // Рекурсивно викликаємо функцію для всіх підкатегорій
//            category.subCategories.forEach { subCategory ->
//                buildPathsForCategories(subCategory, newPath, paths)
//            }
//        }
//
//        return paths
//    }