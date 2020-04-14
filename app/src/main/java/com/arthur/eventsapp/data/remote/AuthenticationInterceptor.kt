package com.arthur.eventsapp.data.remote

import com.arthur.eventsapp.util.MyApplication
import com.arthur.eventsapp.util.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenIntercept = SharedPreferences.getInstance(MyApplication.appContext!!).getString("TOKEN", "")?:""
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", tokenIntercept)
            .header("ContentX-Type", "application/json")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}