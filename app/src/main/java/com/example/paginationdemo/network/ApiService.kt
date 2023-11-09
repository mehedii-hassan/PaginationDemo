package com.example.paginationdemo.network

import com.example.paginationdemo.models.PhotoModel
import com.example.paginationdemo.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiService {

    @Headers("Authorization: Client-ID " + Constants.PHOTOS_API_KEY)
    @GET("photos")
    fun getImageListResponse(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<PhotoModel>>


}