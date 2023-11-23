package com.example.help_code.data.testgateway


import com.example.help_code.data.rest.createNetworkClient
import com.example.help_code.data.rest.getJsonHeader
import com.example.help_code.data.rest.getJsonHeaderVault
import com.example.help_code.presentation.blank.LoginVault
import com.example.help_code.presentation.blank.RedeemCardVault
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

interface ICMSGateway {
    suspend fun getCMSCall(url: String): Response<ResponseBody>
}

class CMSGateway(private val gatewayApi: GatewayApi = createNetworkClient().create(GatewayApi::class.java)) :
    ICMSGateway {
    suspend fun postRedeemCardVault(url: String, body: RedeemCardVault): Response<ResponseBody> {
        return gatewayApi.postRedeemCard(url = url, body, headerMap = getJsonHeaderVault()).getOrThrow()
    }

    suspend fun postLoginVault(url: String, body: LoginVault): Response<ResponseBody> {
        return gatewayApi.postTest(url = url, body, headerMap = getJsonHeaderVault()).getOrThrow()
    }

    override suspend fun getCMSCall(url: String): Response<ResponseBody> {
        return gatewayApi.getTest(url = url, headerMap = getJsonHeader()).getOrThrow()
    }

    private fun Response<ResponseBody>.getOrThrow(): Response<ResponseBody> {
        return if (isSuccessful) this else throw HttpException(this)
    }
}

