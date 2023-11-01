package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class BlankViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val handleError = CoroutineExceptionHandler { _, error ->
        Timber.e(error)
    }

    fun test3() {
        viewModelScope.launch(handleError) {
            runTask()
//            kotlin.runCatching {
////                withContext(Dispatchers.IO) {
//
//                delay(1000)
//                throw HttpRetryException("test exception", 400)
////                }
//
//            }.onSuccess {
//                // do something with success response
//                Timber.d("success call")
//            }.onFailure {
//                Timber.e(it)
//            }

        }
    }

    suspend fun runTask():Boolean {
        delay(1000)
//        throw HttpRetryException("test exception", 400)
        throw Throwable("test exception" )
    }
}