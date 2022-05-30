package com.example.foodies

import androidx.room.*
import com.example.foodies.entity.Foodies
import kotlinx.coroutines.flow.Flow


@Dao
interface FoodiesDao {
    @Insert
    suspend fun insertfavfooddish(foodies: Foodies)
    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<Foodies>>

@Update
suspend fun updatefavfood(foodies: Foodies)

@Query("SELECT * FROM FAV_DISHES_TABLE WHERE favoriteDish=1")
fun getFavoriteDish():Flow<List<Foodies>>

@Delete
suspend fun deleteitem(foodies: Foodies)

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE type = :filterType")
    fun getFilteredDishesList(filterType: String): Flow<List<Foodies>>


}