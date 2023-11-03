package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.help_code.data.testgateway.CMSGateway
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException
import timber.log.Timber

val timber = Timber.tag("BlankViewModel")

class BlankViewModel : ViewModel() {

    private val listUrl = listOf(
        "https://example1.com",
        "https://testApi.com/test",
        "https://rdsig.yahoo.co.jp/smptop/topi/test",
        "https://weu-api-digital-tst.azure-api.net/api/member-details/v2/updates/30218711",
        "https://weu-api-digital-tst.azure-api.net/api/member-details/v2/updates",
        "https://weu-api-digital-tst.azure-api.net/api/member-details/v2",
        "https://klassbook.commonsware.com"
    )
    private val gateway = CMSGateway()

    val handleError = CoroutineExceptionHandler { _, error ->
        timber.e("CoroutineExceptionHandler")
        timber.e(error)
    }

    fun runTask() {
        timber.w("coroutine above")
        viewModelScope.launch(handleError) {
            val result = mergeArray()
            timber.w("Finally result: \n${result.joinToString(separator = "\n")}")
        }
        timber.w("coroutine below")
    }

    private suspend fun mergeArray() = supervisorScope {
        listUrl.map { url ->
            async {
                val urltemp = url
                kotlin.runCatching {
                    gateway.getCMSCall(url)
                }.onFailure {
                    val _url = if (it is HttpException) it.response()
                        ?.raw()?.networkResponse?.request?.url.toString() else urltemp
                    timber.e("failure: ${it.message}, url: ${_url}")
                }.onSuccess {
                    timber.d("success: $it")
                }
            }
        }.awaitAll()
    }
}