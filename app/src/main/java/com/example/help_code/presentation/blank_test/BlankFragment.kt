package com.example.help_code.presentation.blank_test

import android.os.Bundle
import android.view.View
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlank2Binding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class BlankFragment : BaseBindingFragment<FragmentBlank2Binding>(FragmentBlank2Binding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
            println("Handle $exception in CoroutineExceptionHandler")
        }

        val topLevelScope = CoroutineScope(Job())

        topLevelScope.launch {
            launch(coroutineExceptionHandler) {
                throw RuntimeException("RuntimeException in nested coroutine")
            }
        }

        Thread.sleep(100)
    }
}