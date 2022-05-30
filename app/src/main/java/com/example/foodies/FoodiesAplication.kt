package com.example.foodies

import android.app.Application

class FoodiesAplication:Application() {
    private val database by lazy {
        Foodiesdatabase.getDatabase(
            this@FoodiesAplication
        )

    }
    val repository by lazy {
        FoodiesRepository(database.foodiesdao())
    }
}