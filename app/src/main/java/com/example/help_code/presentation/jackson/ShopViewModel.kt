package com.example.help_code.presentation.jackson

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream

class ShopViewModel : ViewModel() {
    private var shopModel: Shop? = null
    private var shopProvider: ShopProvider? = null
    private val _itemLiveData = MutableLiveData<List<Category>?>()
    val itemLiveData: LiveData<List<Category>?> = _itemLiveData
    fun fetchData(input: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            shopModel = ShopPullParse.parse(input)
            val shopProvider = ShopProvider(shopModel)
            shopProvider.buildAsFolder()
//            _itemLiveData.postValue(shopModel?.let { shopProvider.buildCategoriesHierarchy(it.categories) })
        }
    }
}

