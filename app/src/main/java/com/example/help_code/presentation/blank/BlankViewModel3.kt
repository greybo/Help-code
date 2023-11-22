package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel

data class TestModel(val name: String)
class BlankViewModel3 : ViewModel() {
    val list = listOf(listOf(TestModel("a")), listOf(TestModel("b")), listOf(TestModel("c")))
    fun runTask() {
//        val result = list.all { it ?: false }
//        val result = list.reduceRight { s, acc ->
//            s + acc
//        }

        val result = list.toMutableList().elementAtOrElse(100) {
            "element is absent"
        }
        myTimber.w("BlankViewModel3: ${result}")
    }

}