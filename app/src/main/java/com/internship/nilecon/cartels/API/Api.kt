package com.internship.nilecon.cartels.API

import android.content.Context
import com.internship.nilecon.cartels.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {

    fun <T> Declaration(context: Context, service: Class<T>): T {
        return Retrofit.Builder()
                .baseUrl(context.getString(R.string.api_service_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(service)
    }
}