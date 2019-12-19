package com.arindom.imagesearchapp.network

import com.arindom.imagesearchapp.BuildConfig
import com.arindom.imagesearchapp.network.modals.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AppApiHandler {
    private val mImageSearchServices = RetrofitBuilder.createImageSearchServices()
    fun getPhotoes(
        searchtext: String,
        currentPage: Int,
        listener: GenericApiListener<SearchResult.Photos, String>
    ) {
        mImageSearchServices
            .getPhotoesFor(BuildConfig.API_KEYS, searchtext, currentPage, 10)
            .enqueue(object : Callback<SearchResult> {
                override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                    listener.onFailure(t.message ?: "Something went wrong")
                }

                override fun onResponse(
                    call: Call<SearchResult>,
                    response: Response<SearchResult>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.stat == "fail") {
                            listener.onFailure("Please enter valid search key")
                        } else {
                            listener.onSuccess(response.body()!!.photos)
                        }
                    } else {
                        listener.onFailure("Something went wrong")
                    }
                }

            })
    }
}