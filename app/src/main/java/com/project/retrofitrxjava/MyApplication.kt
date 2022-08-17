package com.project.retrofitrxjava

import android.app.Application
import com.project.retrofitrxjava.BuildConfig
import com.project.retrofitrxjava.data.remote.NetworkService
import com.project.retrofitrxjava.data.remote.Networking

class MyApplication : Application(){

    lateinit var networkService: NetworkService

    override fun onCreate() {
        super.onCreate()

        networkService = Networking.create(BuildConfig.BASE_URL,this.cacheDir, 10 * 1024 * 1024 )
    }
}