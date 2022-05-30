package com.example.foodies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.network.Foodiesapiservice
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class Randomfoodvm :ViewModel(){


    private val randomapiservice=Foodiesapiservice()


    private val compositeDisposable=CompositeDisposable()
    val loadfood=MutableLiveData<Boolean>()

    val randomdishresponse=MutableLiveData<RandomDish.Recipies>()
val randomdishloadingerror=MutableLiveData<Boolean>()

    fun getrandomfromapi(){
        loadfood.value=true
        compositeDisposable.add(
          randomapiservice.getrandomdish()
              .subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(object :DisposableSingleObserver<RandomDish.Recipies>(){
                  override fun onSuccess(t: RandomDish.Recipies) {
                      loadfood.value=false
                      randomdishresponse.value=t
                      randomdishloadingerror.value=false
                  }

                  override fun onError(e: Throwable) {
                      loadfood.value=false
                      randomdishloadingerror.value=true
                      e.printStackTrace()
                  }

              })


        )
    }

}