package com.example.foodies.network

import com.example.foodies.RandomDish
import com.example.foodies.ui.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodiesApi {
    @GET(Constants.API_ENDPOINT)
    fun getdishes(
        @Query(Constants.API_KEY) apiKey: String,
        @Query(Constants.LIMIT_LICENSE) limitLicense: Boolean,
        @Query(Constants.TAGS) tags: String,
        @Query(Constants.NUMBER) number: Int

    ):Single<RandomDish.Recipies>





}