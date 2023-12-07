package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel
import timber.log.Timber

data class TestModel(val name: String)
class CollectionViewModel : ViewModel() {

    private val list =
        listOf(listOf(TestModel("a")), listOf(TestModel("b")), listOf(TestModel("c")))

    fun runTask() {
        val resultAll = list.all { it[0] != null }
        Timber.d("result all: $resultAll")
        val resultReduce = list.reduceRight { s, acc ->
            s + acc
        }
        Timber.d("result reduce: $resultReduce")

        val result = list.toMutableList().elementAtOrElse(100) {
            "element is absent"
        }
        myTimber.w("BlankViewModel3: ${result}")
    }

}