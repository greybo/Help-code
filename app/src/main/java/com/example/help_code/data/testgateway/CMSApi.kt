package com.example.help_code.data.testgateway

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Url

interface CMSApi {
    @GET
    suspend fun getTest(@Url url: String, @HeaderMap headerMap: Map<String, String>): Response<ResponseBody>
}