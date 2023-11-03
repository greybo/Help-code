package com.example.help_code.data.testgateway


import com.example.help_code.data.rest.createNetworkClient
import com.example.help_code.data.rest.getJsonHeader
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

interface ICMSGateway {
    suspend fun getCMSCall(url: String): Response<ResponseBody>
}

class CMSGateway(private val cmsApi: CMSApi = createNetworkClient().create(CMSApi::class.java)) :
    ICMSGateway {
    override suspend fun getCMSCall(url: String): Response<ResponseBody> {
        return cmsApi.getTest(url = url, headerMap = getJsonHeader()).getOrThrow()
    }

    private fun Response<ResponseBody>.getOrThrow(): Response<ResponseBody> {
        return if (isSuccessful) this else throw HttpException(this)
    }
}

