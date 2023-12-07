package com.example.help_code.presentation.blank

import com.example.help_code.base.CompositeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class ExceptionCoroutineViewModel : CompositeViewModel() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun runTask() {
        scope.launch {
            try {
                taskException()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

   private suspend fun taskException() {
        delay(1000)
        throw NullPointerException()
    }
}