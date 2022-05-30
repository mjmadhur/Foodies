package com.example.foodies.network

import com.example.foodies.RandomDish
import com.example.foodies.ui.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class Foodiesapiservice {

    val api=Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(FoodiesApi::class.java)
    fun getrandomdish():Single<RandomDish.Recipies>{
        return api.getdishes(
            Constants.API_VALUE,
            Constants.LIMIT_LICENSE_VALUE,
            Constants.TAGS_VALUE,Constants.NUMBER_VALUE
        )
    }
}