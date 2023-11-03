package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.help_code.data.testgateway.CMSGateway
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.HttpRetryException
import java.net.UnknownServiceException

val timber = Timber.tag("BlankViewModel")

class BlankViewModel : ViewModel() {

    private val listUrl = listOf(
        "https://testApi.com/test",
        "https://rdsig.yahoo.co.jp/smptop/topi/test",
        "https://weu-api-digital-tst.azure-api.net/api/member-details/v2/updates/30218711",
        "https://klassbook.commonsware.com"
    )
    private val gateway = CMSGateway()

    val handleError = CoroutineExceptionHandler { _, error ->
        timber.e(error)
    }

    fun mainTest() {
        timber.w("mainTest Start")
        viewModelScope.launch {
//           val result =  mergeArray()
//            timber.w("end: $result")

            mergeArray2()
        }
//        timber.w("end")
    }

    private suspend fun mergeArray() = supervisorScope {
        val mapResult = listUrl.map { url ->
            async {
                try {
                    val response = gateway.getCMSCall(url)
                    if (response.code() == 200) {
                        response
                    } else throw HttpException(response)
                } catch (e: UnknownServiceException) {
                    timber.w("TryCatch UnknownServiceException: ${e}")
                    "UnknownServiceException $url"
                } catch (e: IOException) {
                    timber.w("TryCatch IOException: ${e}")
                    "IOException $url"
                } catch (error: HttpException) {
                    timber.w("TryCatch HttpException: ${error}")
                    "HttpException  ${error.response()?.raw()?.request?.url.toString()}"
                } catch (e: Throwable) {
                    timber.w("TryCatch Throwable: $e")
                    "Throwable $url"
                }
            }
        }.awaitAll()

        timber.i("Result end -> ${mapResult}")
        true
    }

    suspend fun mergeArray2() = runBlocking {
        val result = listUrl.map { url ->
//            supervisorScope {
            async {
                kotlin.runCatching {
                    val response = gateway.getCMSCall(url)
                    if (response.code() == 200) {
                        response
                    } else throw HttpException(response)
                }.onSuccess {
                    // do something with success response
                    timber.d("success call: $it")
                }.onFailure {
                    timber.e("runCatching: $it")
                }
//                }
            }
        }.awaitAll()
        timber.w("finally result: $result")
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
}