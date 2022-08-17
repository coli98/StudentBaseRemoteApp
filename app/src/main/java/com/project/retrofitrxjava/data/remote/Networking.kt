package com.project.retrofitrxjava.data.remote

import com.afollestad.materialdialogs.BuildConfig
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.util.concurrent.TimeUnit

object Networking {
    private const val NETWORKING_TIMEOUT = 60
    private lateinit var okHttpClient: OkHttpClient

    fun create(baseUrl:String, casheDir: File, casheSize: Long): NetworkService{

        okHttpClient = OkHttpClient.Builder()
            .cache(Cache(casheDir,casheSize))
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                }
            )
            .readTimeout(NETWORKING_TIMEOUT.toLong(),TimeUnit.SECONDS)
            .writeTimeout(NETWORKING_TIMEOUT.toLong(),TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }

}