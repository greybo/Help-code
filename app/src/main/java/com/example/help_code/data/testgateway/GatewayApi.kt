package com.example.help_code.data.testgateway

import com.example.help_code.presentation.blank.LoginVault
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface GatewayApi {
    @GET
    suspend fun getTest(@Url url: String, @HeaderMap headerMap: Map<String, String>): Response<ResponseBody>

    @POST
    suspend fun postTest(@Url url: String, @Body body: LoginVault, @HeaderMap headerMap: Map<String, String>): Response<ResponseBody>
}