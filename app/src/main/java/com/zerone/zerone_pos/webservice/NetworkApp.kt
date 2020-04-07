package com.zerone.zerone_pos.webservice

import android.content.Context
import com.zerone.zerone_pos.helpers.Config.BASE_URL
import com.zerone.zerone_pos.helpers.SharedPrefControl
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkApp {
    var cacheSize = 10 * 1024 * 1024 // 10 MB
    var cache = Cache(createTempDir(), cacheSize.toLong())

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method(), original.body())

            val request = requestBuilder.build()
            chain.proceed(request)
        }.cache(cache).build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val ZERONE_POS_SERVICE: ZeronePosService = retrofit.create(ZeronePosService::class.java)


    fun api(): ZeronePosService {
        return retrofit.create(ZeronePosService::class.java)
    }
}