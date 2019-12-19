package com.arindom.imagesearchapp.network

import com.arindom.imagesearchapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private fun retrofitBuilder(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createImageSearchServices(): ImageSerchServices =
        retrofitBuilder(BuildConfig.Base_Url).create(ImageSerchServices::class.java)

}
