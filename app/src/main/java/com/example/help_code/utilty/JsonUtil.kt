package com.example.help_code.utilty

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.internal.LinkedTreeMap
import retrofit2.HttpException
import retrofit2.Response


private fun <T> handleResponse(
    response: Response<LinkedTreeMap<String?, String?>>,
    clazz: Class<T>
): T {
    if (!response.isSuccessful) throw HttpException(response)

    val gson = GsonBuilder().setLenient().create()
    val jsonObject: JsonObject = gson.toJsonTree(response.body()).asJsonObject
    return gson.fromJson(jsonObject, clazz)
}