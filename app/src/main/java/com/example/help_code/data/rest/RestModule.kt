package com.example.help_code.data.rest

import android.content.Context
import com.example.help_code.HelpCodeApplication
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val cacheSize = (10 * 1024 * 1024).toLong()

fun createNetworkClient(): Retrofit {
    return Retrofit.Builder()
//        .baseUrl(Configuration.shared.property.digitalURL + "/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    //.registerTypeAdapter(StructuredApiModel::class.java, StructuredDeserializer<StructuredApiModel>())
                    .create()
            )
        )
        .client(createOkHttpClient(HelpCodeApplication.instance))
        .build()
}

fun createOkHttpClient(context: Context): OkHttpClient {
    val builder = OkHttpClient.Builder()
//    if (BuildConfig.DEBUG) {
    builder.addInterceptor(
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)
//                .setLevel(HttpLoggingInterceptor.Level.BODY)
    )
//    }
    builder.addInterceptor(timeoutInterceptor())
    //builder.addInterceptor(connectivityInterceptor())
    builder.connectTimeout(15, TimeUnit.SECONDS)
    builder.readTimeout(15, TimeUnit.SECONDS)
    builder.writeTimeout(15, TimeUnit.SECONDS)
    //Disable cache for debug and uat builds
//    if (BuildConfig.BUILD_TYPE == BuildTypes.PROD.value) {
//        //builder.addInterceptor(onlineInterceptor())
//        builder.cache(Cache(context.cacheDir, cacheSize))
//    }
    return builder.build()
}

@Suppress("unused")
fun onlineInterceptor() = Interceptor {
    val request = it.request()
    it.proceed(
        when (request.method) {
            "GET" -> {
                val cacheControl = CacheControl.Builder()
                    .maxAge(5, TimeUnit.MINUTES) // 5 minutes cache
                    .build()
                request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }

            else -> request
        }
    )
}

//Set timeout for release version call
fun timeoutInterceptor() = Interceptor {
    val request = it.request()
    val duration = getTimeoutDuration(request)
    if (duration > 0) {
        it.withReadTimeout(duration, TimeUnit.MILLISECONDS)
            .withConnectTimeout(duration, TimeUnit.MILLISECONDS)
            .withWriteTimeout(duration, TimeUnit.MILLISECONDS)
            .proceed(request)
    } else {
        it.proceed(request)
    }
}

@Suppress("unused")
fun connectivityInterceptor() = Interceptor {
    val request = it.request()
    if (!hasInternetConnection()) {
        throw CustomException.NoInternetException()
    } else {
        it.proceed(request)
    }
}

fun getTimeoutDuration(request: Request): Int {
    val tag = request.tag(retrofit2.Invocation::class.java)
    val timeout = tag?.method()?.getAnnotation(Timeout::class.java)
    return timeout?.duration ?: 0
}