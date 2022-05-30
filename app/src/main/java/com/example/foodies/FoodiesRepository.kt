package com.example.foodies

import androidx.annotation.WorkerThread
import com.example.foodies.entity.Foodies
import kotlinx.coroutines.flow.Flow

class FoodiesRepository(private  val  foodiesDao:FoodiesDao) {
    @WorkerThread
    suspend fun insertfood(foodies: Foodies){
        foodiesDao.insertfavfooddish(foodies)
    }
    val allDishesList: Flow<List<Foodies>> = foodiesDao.getAllDishesList()
@WorkerThread
suspend fun updatefooddata(foodies: Foodies){
    foodiesDao.updatefavfood(foodies)
}
    val favfoodlist:Flow<List<Foodies>> =foodiesDao.getFavoriteDish()
@WorkerThread
suspend fun deleteitem(foodies: Foodies){
    foodiesDao.deleteitem(foodies)
}
    fun filteredListDishes(value: String): Flow<List<Foodies>> =
        foodiesDao.getFilteredDishesList(value)

}