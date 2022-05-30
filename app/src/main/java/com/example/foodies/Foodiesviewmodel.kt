package com.example.foodies

import android.view.View
import androidx.lifecycle.*
import com.example.foodies.entity.Foodies
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class Foodiesviewmodel(private val repository: FoodiesRepository):ViewModel() {
    fun insert(dish:Foodies)=viewModelScope.launch {
        repository.insertfood(dish)
    }
    val alldisheslist:LiveData<List<Foodies>> =repository.allDishesList.asLiveData()
fun updatefoodies(food: Foodies)=viewModelScope.launch {
    repository.updatefooddata(food)
}
    val favfoodlist:LiveData<List<Foodies>> =repository.favfoodlist.asLiveData()
fun deleteitem(fooditem: Foodies)=viewModelScope.launch{
    repository.deleteitem(fooditem)
}
    fun getfilterlist(value:String):LiveData<List<Foodies>> =repository.filteredListDishes(value).asLiveData()


}
class foodiesvmfactory(private val repository: FoodiesRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(Foodiesviewmodel::class.java)){
            return Foodiesviewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}