package com.example.foodies

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodies.entity.Foodies

@Database(entities = [Foodies::class],version = 2,)

abstract  class Foodiesdatabase : RoomDatabase() {
    abstract fun foodiesdao():FoodiesDao
    companion object {

        @Volatile
        private var INSTANCE: Foodiesdatabase? = null

        fun getDatabase(context: Context): Foodiesdatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Foodiesdatabase::class.java,
                    "fav_dish_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}