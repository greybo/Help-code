package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import timber.log.Timber
import java.net.HttpRetryException

class BlankViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val handleError = CoroutineExceptionHandler { _, error ->
        Timber.e(error)
    }

    fun mainTest() {
        viewModelScope.launch {
            Timber.d("Start")
            test()
//            runCatchingTest()
//            doWorld2()

            Timber.d("Done")
        }
    }

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
            async { runTaskException("two", 2000) },//.await(),
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

    //    private suspend fun runTask(s: String, time: Long): String {
//        return withContext(Dispatchers.IO) {
//            Timber.i("start: $s")
//            kotlinx.coroutines.delay(time)
//            s
//        }
//    }
    suspend fun runTasksWithException() = coroutineScope { // this: CoroutineScope
        Timber.w("Must complete 3 tasks with exception")
        launch { runTask("task 1", 3000) }
        try {
            launch { runTaskException("task 2", 4000) }
        } catch (e: Exception) {
            Timber.e("try with exception:", e)
        }
        launch { runTask("task 3", 5000) }
    }

    suspend fun doTasks() = coroutineScope { // this: CoroutineScope
        Timber.w("Must complete 2 tasks with first exception")
        async {
            runTaskException("Task 1", 5000L)
        }.await()
        async {
            runTask("Task 2", 1000L)
        }.await()
        Timber.d("Hello")
    }


    suspend fun runCatchingTest() = runBlocking {

        kotlin.runCatching {

            listTasks.map {
                async {
                    it.run()
                }
            }.awaitAll().all { it }

//            runTasksWithException()
//            async {
//                runTaskException("Task 1", 5000L)
//            }.await()

        }.onSuccess {
            // do something with success response
            Timber.d("success call: $it")
        }.onFailure {
            Timber.e("runCatching: $it")
        }
    }
}

val listFlow = flowOf(
    RunTask.Simple("Task 1", 5000L),
    RunTask.Exception("Task 2", 3000L),
    RunTask.Simple("Task 3", 5000L),
)
val listTasks = listOf(
    RunTask.Simple("Task 1", 5000L),
    RunTask.Exception("Task 2", 3000L),
    RunTask.Simple("Task 3", 5000L),
)
val list = listOf(
    Pair("one", 4000L),
    Pair("two", 5000L),
    Pair("three", 100L),
)

sealed class RunTask() {
    abstract suspend fun run(): Boolean

    class Simple(private val message: String, private val delay: Long) : RunTask() {
        override suspend fun run(): Boolean {
            runTask(message, delay)
            return true
        }
    }

    class Exception(private val message: String, private val delay: Long) : RunTask() {
        override suspend fun run(): Boolean {
            runTaskException(message, delay)
            return true
        }
    }
}

suspend fun runTask(message: String, delay: Long = 1000): Boolean {
    delay(delay)
    Timber.d(message)
    return true
}

suspend fun runTaskException(message: String, delay: Long = 1000): Boolean {
    delay(delay)
    throw HttpRetryException("test exception: ${message}", 400)
//        throw Throwable("test exception")
}