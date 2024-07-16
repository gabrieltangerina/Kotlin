package com.example.burgershub.network

import android.content.Context
import io.github.brunogabriel.mockpinterceptor.MockPInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ServiceProvider @Inject constructor(
    context: Context
) {

    private val baseUrl = "https://burgers-hub.p.rapidapi.com/"

    private val mockpInterceptor = MockPInterceptor
        .Builder(context)
        .addDelayInMillis(2_000L, 4_000L)
        .build()

    /*
    * Configurando serviço RetroFit e cliente OKHttp
    */
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(Interceptor { chain ->
            chain.run {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader(
                            "x-rapidapi-key",
                            "c5a5f6e406mshe9784b4c1fb7fa1p1e5cd7jsn63a0059ab09e")
                        .build()
                )
            }
        })
        .addInterceptor(mockpInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <API> createService(apiClass: Class<API>): API = retrofit.create(apiClass)

}