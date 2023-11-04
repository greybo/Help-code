package com.example.help_code.presentation.blank.coroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

data class TimeModel(val time: Long, val name: String)

val listTime = listOf(
    TimeModel(1000L, "task 1"),
    TimeModel(2000L, "task 2"),
    TimeModel(1000L, "task 3"),
    TimeModel(3000L, "task 4"),
    TimeModel(5000L, "task 5")
)

class LatestNewsViewModel(

) : ViewModel() {

    private val myTimber2 = Timber.tag("LatestNewsViewModel")

    private val newsRepository = NewsRepository()

    fun fetch() {
        viewModelScope.launch {
            listTime.map {
                newsRepository.fetchTest(it)
                    // Intermediate catch operator. If an exception is thrown,
                    // catch and update the UI
                    .catch { exception -> notifyError(exception) }
                    .collect { data ->
                        // Update View with the latest favorite news
                        myTimber2.i("collect: ${data}")
                    }
            }
        }
    }

    private fun notifyError(error: Throwable) {
        myTimber2.i("catch:  ")
        if (error is HttpException) {
            myTimber2.e("code: ${error.code()}")
            myTimber2.e(error)
        } else {
            myTimber2.e(error)
        }
    }
}