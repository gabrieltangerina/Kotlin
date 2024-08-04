package com.example.movieapp.data.interceptor

import com.example.movieapp.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl: HttpUrl = originalRequest.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY, BuildConfig.API_KEY)
            .build()

        val requestBuilder = originalRequest.newBuilder().url(url)
        val request = requestBuilder.build()

        return chain.proceed(request)
    }

    companion object {
        private const val API_KEY = "api_key"
    }

}