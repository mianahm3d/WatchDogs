package com.fourun.watchdogs.utils

import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

object ApiService {
    private const val BASE_URL = "https://sunpra-auth.herokuapp.com" // Replace with your actual base URL

    fun getService(): ApiConsumer {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // Add the OkHttpClient to the Retrofit builder

        val retrofit = builder.build()
        return retrofit.create(ApiConsumer::class.java)
    }
}

// Define the APIConsumer interface
interface ApiConsumer {
    // Define your API endpoints here
}