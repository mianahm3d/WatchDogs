package com.fourun.watchdogs.utils

import okhttp3.Response
import retrofit2.http.POST

interface APIConsumer {
    @POST("users/validate-unique-email")
    fun validateEmailAddress() : Response
}