package com.arindom.imagesearchapp.network

import com.arindom.imagesearchapp.network.modals.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageSerchServices {
    //?method=flickr.photos.search&api_key=1043b2779d7b7b88da9e30e92f1693d5&text=human&safe_search=1&page=1&per_page=20&format=json&nojsoncallback=1
    @GET("rest?method=flickr.photos.search&format=json&nojsoncallback=1&safe_search=1")
    fun getPhotoesFor(
        @Query("api_key") apiKey: String,
        @Query("text") text: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ):Call<SearchResult>

}