package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.help_code.data.testgateway.CMSGateway
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.HttpException
import timber.log.Timber

const val host = "vault-esp-staging-au-kvwr4chkta-ts.a.run.app"

class VaultViewModel : ViewModel() {

    val myTimber = Timber.tag("BlankViewModel")

    val url = "https://vault-esp-staging-au-kvwr4chkta-ts.a.run.app/v1/users/login"


    private val gateway = CMSGateway()

    val handleError = CoroutineExceptionHandler { _, error ->
        myTimber.e("CoroutineExceptionHandler")
        myTimber.e(error)
    }

    fun runTask() {
        myTimber.w("coroutine above")
        viewModelScope.launch(handleError) {
            kotlin.runCatching {
                myTimber.w("request above")
                val response = gateway.postVault(url, LoginVault())
                myTimber.w("request: ${response.body()}")
                myTimber.w("request below")
            }.onFailure {
                val _url = if (it is HttpException) it.response()
                    ?.raw()?.networkResponse?.request?.url.toString() else url
                myTimber.e("failure: ${it.message}, url: ${_url}")
            }.onSuccess {
                myTimber.w("success: $it")
            }
        }
        myTimber.w("coroutine below")
    }

    private suspend fun mergeArray() = supervisorScope {

    }

}

data class LoginVault(val username: String = "sergeybotl@gmail.com", val password: String = "@Sergey29")