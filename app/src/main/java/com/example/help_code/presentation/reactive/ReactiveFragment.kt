package com.example.help_code.presentation.reactive


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.help_code.databinding.FragmentReactiveBinding
import com.example.help_code.utilty.createBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

//class ReactiveFragment : BaseBindingFragment<FragmentReactiveBinding>(FragmentReactiveBinding::inflate) {
class ReactiveFragment : Fragment() {

    lateinit var binding: FragmentReactiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = createBinding(FragmentReactiveBinding::inflate)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        test3()
//        test2()
//        test1()
//        test0()
        binding.reactiveDisplayedText.text = "Hello binding"

    }

    fun test3() = runBlocking<Unit> {
        val a = (1..5).asFlow()
        val b = (6..10).asFlow()
        val c = (11..19).asFlow()
        a
            .zip(b) { x, y -> "zip b: $x + $y" }
            .zip(c) { x, y -> "zip c: $x + $y" }
            .collect { println(it) }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun test2() = runBlocking<Unit> {
        val flow1 = flowOf("A", "B", "C")
        val flow2 = flowOf("D", "E", "F")
        val flow3 = flowOf("G", "H", "I")

        flowOf(flow1, flow2, flow3)
            .flatMapConcat { it }
            .collect {
                delay(500)
                println("flatMapConcat: $it")
            }
    }


    fun test1() = runBlocking<Unit> {
        val numbers = (1..5).asFlow()
        numbers.transform { value ->
            emit("A$value")
            emit("B$value")
            delay(500)
        }.collect { println("transform: $it") }
    }

    fun test(): Flow<Int> = flow {
        try {
            emit(1)
            emit(2)
            println("This line will not execute")
            emit(3)
        } finally {
            println("Finally in numbers")
        }
    }

    fun test0() = runBlocking<Unit> {
        test().take(2) // take only the first two
            .collect { value ->
                delay(500)
                println("take: $value")
            }
    }

    private fun println(value: String?) {
        Timber.i("ReactiveFragment: $value")
    }
}