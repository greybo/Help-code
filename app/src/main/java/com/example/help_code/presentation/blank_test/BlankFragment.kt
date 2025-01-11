package com.example.help_code.presentation.blank_test

import android.os.Bundle
import android.view.View
import com.example.help_code.base.BaseBindingFragment
import com.example.help_code.databinding.FragmentBlank2Binding
import timber.log.Timber
import java.sql.Timestamp


class BlankFragment : BaseBindingFragment<FragmentBlank2Binding>(FragmentBlank2Binding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val oneMinute = 60 * 1000
        val date1 = System.currentTimeMillis()
        val date2 = System.currentTimeMillis() + 3 * oneMinute
        val different = date2 - date1
        val differentTimestamp = Timestamp(different)

        val timeStamp1 = Timestamp(date1)
        val timeStamp2 = Timestamp(date2)

        Timber.i(
            "timeStamp1: $date1, \n" +
                    "timeStamp2: $date2, \n" +
                    "compare1: ${date1 > date2}, \n" +
                    "compare2: ${date2 > date1}, \n" +
                    "different: ${different}, \n" +
                    "differentTime: ${differentTimestamp}, \n"

        )
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
//            println("Handle $exception in CoroutineExceptionHandler")
//        }
//
//        val topLevelScope = CoroutineScope(Job())
//
//        topLevelScope.launch {
//            launch(coroutineExceptionHandler) {
//                throw RuntimeException("RuntimeException in nested coroutine")
//            }
//        }
//
//        Thread.sleep(100)
//    }
}