package com.example.help_code.presentation.blank

import androidx.lifecycle.ViewModel
import com.example.help_code.data.testgateway.CMSGateway
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber

const val host = "vault-esp-staging-au-kvwr4chkta-ts.a.run.app"

class VaultViewModel : ViewModel() {

    val myTimber = Timber

    val urlCreateRedeem = "https://vault-esp-staging-au-kvwr4chkta-ts.a.run.app/v1/cards/create-redeemed"
    val urlRedeem = "https://vault-esp-staging-au-kvwr4chkta-ts.a.run.app/v1/cardholder/redeem/sms"
    val urlLogin = "https://vault-esp-staging-au-kvwr4chkta-ts.a.run.app/v1/users/login"
//    val url = "https://rdsig.yahoo.co.jp/smptop/topi/test"


    private val gateway = CMSGateway()

    val handleError = CoroutineExceptionHandler { _, error ->
        myTimber.e("CoroutineExceptionHandler")
        myTimber.e(error)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun runTask() {
        myTimber.w("coroutine above")
        GlobalScope.launch(Dispatchers.IO) {
            tryCall()
        }
        myTimber.w("coroutine below")
    }

    private suspend fun tryCall() = runBlocking {
        myTimber.w("request above")
        try {

//            val response = gateway.postRedeemCardVault(urlRedeem, RedeemCardVault())
            val response = gateway.postLoginVault(urlLogin, LoginVault())
            myTimber.w("request: ${response.body()}")

        } catch (e: Exception) {
            val _url = if (e is HttpException) e.response()
                ?.raw()?.networkResponse?.request?.url.toString() else "url"
            myTimber.e("failure: ${e.message}, url: ${_url}")
        }
        myTimber.w("request below")
    }
}

data class LoginVault(val username: String = "sergeybotl@gmail.com", val password: String = "@Sergey29")
data class RedeemCardVault(val phone: String = "+61 555555555", val code: String = "123456", val email: String = "sergeybotl@gmail.com")