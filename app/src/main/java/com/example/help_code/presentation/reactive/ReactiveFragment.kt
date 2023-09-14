package com.example.help_code.presentation.reactive


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.help_code.databinding.FragmentReactiveBinding
import com.example.help_code.utilty.createBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

//class ReactiveFragment : BaseBindingFragment<FragmentReactiveBinding>(FragmentReactiveBinding::inflate) {
class ReactiveFragment : Fragment() {

    lateinit var binding: FragmentReactiveBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = createBinding(FragmentReactiveBinding::inflate)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        test2()
        binding.reactiveDisplayedText.text = "Hello binding"

    }

    fun test3() = runBlocking<Unit> {
        val a = (1..5).asFlow()
        val b = (6..10).asFlow()
        val c = (11..19).asFlow()
        a.zip(b) { x, y -> "$x + $y" }.zip(c) { x, y -> "$x + $y" }.collect { println(it) }
    }

    @OptIn(FlowPreview::class)
    fun test2() = runBlocking<Unit> {
        val flow1 = flowOf("A", "B", "C")
        val flow2 = flowOf("D", "E", "F")
        val flow3 = flowOf("G", "H", "I")

        flowOf(flow1, flow2, flow3)
            .flatMapConcat {
             it
            }
            .collect {
                delay(500)
                println(it)
            }
    }


    fun test1() = runBlocking<Unit> {
        val numbers = (1..5).asFlow()
        numbers.transform { value ->
            emit("A$value")
            emit("B$value")
            delay(500)
        }.collect { println(it) }
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

    fun main() = runBlocking<Unit> {
        test().take(2) // take only the first two
            .collect { value ->
                delay(500)
                println(value)
            }
    }

    private fun println(value: String?) {
        Log.i("ReactiveFragment", value ?: "")
    }
}