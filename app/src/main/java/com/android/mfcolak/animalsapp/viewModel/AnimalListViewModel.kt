package com.android.mfcolak.animalsapp.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.mfcolak.animalsapp.model.Animal
import com.android.mfcolak.animalsapp.service.AnimalApiService
import com.android.mfcolak.animalsapp.service.AnimalDatabase
import com.android.mfcolak.animalsapp.util.CustomSharedPreferences
import com.android.mfcolak.animalsapp.util.NotificationHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class AnimalListViewModel(application: Application): BaseViewModel(application) {

    val animals = MutableLiveData<List<Animal>>()
    val errorMessage = MutableLiveData<Boolean>()
    val animalLoading = MutableLiveData<Boolean>()
    private var updateTime = 5 * 60 * 1000 * 1000 * 1000L

    private val animalApiService = AnimalApiService()
    private val disposable = CompositeDisposable()
    private val customSharedPreferences = CustomSharedPreferences(getApplication())

    fun refleshData(){

        checkCacheTime()
        val time = customSharedPreferences.getCurrentTime()
        if (time != null && time != 0L && System.nanoTime() - time < updateTime)    //fetch data from remote after 5 minutes
        {
            fetchFromSQLite()
        }else{

            fetchFromRemote()
        }
    }

    private fun checkCacheTime() {
        val cachePreference = customSharedPreferences.getCacheSize()

        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5
            updateTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }


    fun refleshFromInternet(){
        fetchFromRemote()
    }

    private fun fetchFromSQLite(){
        animalLoading.value = true
        launch {
            val animalList = AnimalDatabase(getApplication()).animalDAO().getAll()
            showAnimals(animalList)
            Toast.makeText(getApplication(), "Fetch data from ROOM", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchFromRemote(){
        animalLoading.value = true

        disposable.add(
            animalApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>(){
                    override fun onSuccess(t: List<Animal>) {
                        keepInSqlite(t)

                        Toast.makeText(getApplication(), "Fetch data from INTERNET", Toast.LENGTH_LONG).show()
                        NotificationHelper(getApplication()).createAnimalNotification()
                    }

                    override fun onError(e: Throwable) {
                       errorMessage.value = true
                        animalLoading.value = false
                        e.printStackTrace()

                    }
                })
        )
    }

    private fun showAnimals(animalList: List<Animal>){
        animals.value = animalList
        errorMessage.value = false
        animalLoading.value = false
    }

    private fun keepInSqlite(animalList: List<Animal>){
        //Coroutine for sync thread
        launch {
            val dao = AnimalDatabase(getApplication()).animalDAO()
            dao.deleteAll()
           val uuidList = dao.insertAll(*animalList.toTypedArray())   //get data in array one by one and add to db with uuid
            var i = 0
            while (i < animalList.size){
                animalList[i].uuid = uuidList[i].toInt()    //  Animal uuid = in SQLite uuid
                i += 1
            }
            showAnimals(animalList)
        }

        customSharedPreferences.saveCurrentTime(System.nanoTime())
    }



}