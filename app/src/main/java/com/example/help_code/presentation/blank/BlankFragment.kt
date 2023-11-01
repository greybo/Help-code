package com.example.help_code.presentation.blank

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlankBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber

class BlankFragment : BaseBindingFragment<FragmentBlankBinding>(FragmentBlankBinding::inflate) {

    private val viewModel: BlankViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        test2()
        viewModel.test3()
//        binding.blankFragmentButton.setOnClickListener { }
    }

    val list = listOf(
        Pair("one", 4000L),
        Pair("two", 5000L),
        Pair("three", 100L),
    )

    fun test2() = runBlocking<Unit> {
        val map = list.map {
            async { runTask(it.first, it.second) }
        }.awaitAll()
        Timber.i("index -> ${map}")
    }

    fun test() = runBlocking<Unit> {
        val nums = (1..3).asFlow() // numbers 1..3
        val strs = flowOf(
            async { runTask("one", 1000) },//.await(),
            async { runTask("two", 2000) },//.await(),
            async { runTask("three", 100) },//.await()
        ) // strings
        nums.zip(strs) { index, data ->
            Timber.i("$index -> ${data.await()}")
            index != 3
        } // compose a single string
            .collect {
                Timber.i(it.toString())
            } // collect and print
    }

    private suspend fun runTask(s: String, time: Long): String {
        return withContext(Dispatchers.IO) {
            Timber.i("start: $s")
            kotlinx.coroutines.delay(time)
            s
        }
    }
}
