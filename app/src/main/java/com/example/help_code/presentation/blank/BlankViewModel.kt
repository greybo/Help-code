package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.help_code.data.testgateway.CMSGateway
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import timber.log.Timber
import java.net.HttpRetryException

val timber = Timber.tag("BlankViewModel")

class BlankViewModel : ViewModel() {

    val gateway = CMSGateway()

    val handleError = CoroutineExceptionHandler { _, error ->
        timber.e(error)
    }

    fun mainTest() {
        timber.w("mainTest Start")
        viewModelScope.launch {
            test2()
//            test()
//            runCatchingTest()
//            doWorld2()
        }
        timber.w("end")
    }

    private suspend fun testGateway() = supervisorScope {

        val mapResult = list.mapIndexed { index, pair ->
            async {
                if (index != 2) {
                    runTask("${pair.first} - ${pair.second}", pair.second)
                } else {
                    runTaskException("${pair.first} - ${pair.second}", pair.second)
                }
            }
        }.awaitAll()

        mapResult.filter {
            it.isFailure
        }.map {
            timber.e("filter error: ${it.exceptionOrNull()}")
        }

        timber.i("index -> ${mapResult}")
        true
    }

    private suspend fun test2() = supervisorScope {

        val mapResult = list.mapIndexed { index, pair ->
            async {
                if (index != 2) {
                    runTask("${pair.first} - ${pair.second}", pair.second)
                } else {
                    runTaskException("${pair.first} - ${pair.second}", pair.second)
                }
            }
        }.awaitAll()

        mapResult.filter {
            it.isFailure
        }.map {
            timber.e("filter error: ${it.exceptionOrNull()}")
        }

        timber.i("index -> ${mapResult}")
        true
    }


    fun test() = runBlocking<Unit> {
        val nums = (1..3).asFlow() // numbers 1..3
        val strs = flowOf(
            async { runTask("one", 1000) },//.await(),
            async { runTaskException("two", 2000) },//.await(),
            async { runTask("three", 100) },//.await()
        ) // strings
        nums.zip(strs) { index, data ->
            timber.i("$index -> ${data.await()}")
            index != 3
        } // compose a single string
            .collect {
                timber.i(it.toString())
            } // collect and print
    }

    //    private suspend fun runTask(s: String, time: Long): String {
//        return withContext(Dispatchers.IO) {
//            timber.i("start: $s")
//            kotlinx.coroutines.delay(time)
//            s
//        }
//    }
    suspend fun runTasksWithException() = coroutineScope { // this: CoroutineScope
        timber.w("Must complete 3 tasks with exception")
        launch { runTask("task 1", 3000) }
        try {
            launch { runTaskException("task 2", 4000) }
        } catch (e: Exception) {
            timber.e("try with exception: $e")
        }
        launch { runTask("task 3", 5000) }
    }

    suspend fun doTasks() = coroutineScope { // this: CoroutineScope
        timber.w("Must complete 2 tasks with first exception")
        async {
            runTaskException("Task 1", 5000L)
        }.await()
        async {
            runTask("Task 2", 1000L)
        }.await()
        timber.d("Hello")
    }


    suspend fun runCatchingTest() = runBlocking {

        kotlin.runCatching {
            supervisorScope {
                listTasks.map {
                    async {
                        it.run()
                    }
                }.awaitAll().all { it }
            }


//            runTasksWithException()
//            async {
//                runTaskException("Task 1", 5000L)
//            }.await()

        }.onSuccess {
            // do something with success response
            timber.d("success call: $it")
        }.onFailure {
            timber.e("runCatching: $it")
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
    Pair("one", 2000L),
    Pair("two", 1000L),
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

suspend fun runTask(message: String, delay: Long = 1000): Result<Boolean> {
    delay(delay)
    timber.d(message)
    return Result.success(true)
}

suspend fun runTaskException(message: String, delay: Long = 1000): Result<Throwable> {
    return runCatching {
        delay(delay)
        throw HttpRetryException("test exception: ${message}", 400)
//        throw Throwable("test exception")
    }

//    return try {
//
//
//    } catch (e: Throwable) {
//        timber.e("runTaskException: $e")
//        e
//    }
}