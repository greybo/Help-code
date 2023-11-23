package com.example.help_code.data.rest

import com.google.gson.annotations.SerializedName

fun getJsonHeaderVault() = mapOf(Header.contentType to "application/json", "Accept" to "application/json")
fun getJsonHeader() = mapOf(Header.contentType to "application/json", "accept" to "application/json")

fun getBaseHeader(): Map<String, String> {
    return getJsonHeader().toMutableMap().apply {
        this[Header.subscriptionKey] = "dsferoiwuero342342"
    }.toMap()
}

fun getAccessHeader(accessToken: String): Map<String, String> {
    return getBaseHeader().toMutableMap().apply {
        this[Header.authorization] = "Bearer $accessToken"
    }.toMap()
}

fun getNoCacheHeader(accessToken: String): Map<String, String> {
    return getAccessHeader(accessToken).toMutableMap().apply {
        this[Header.cacheControl] = "no-cache"
    }.toMap()
}

fun getTokenBody(token: String): Map<String, String> {
    return mapOf("RefreshToken" to token)
}

fun getLoginBody(isPin: Boolean, encryptedPin: String, toFA: Boolean = false): LoginRequest {
    return if (isPin) {
        LoginRequest(type = "PIN", value = encryptedPin, forPi = toFA)
    } else {
        LoginRequest(type = "Password", value = encryptedPin, forPi = toFA)
    }
}

data class LoginRequest(
    @SerializedName("type") val type: String,
    @SerializedName("value") val value: String,
    @SerializedName("forPi") val forPi: Boolean = true
)

