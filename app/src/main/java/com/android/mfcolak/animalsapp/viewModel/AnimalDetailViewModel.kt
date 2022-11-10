package com.android.mfcolak.animalsapp.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.mfcolak.animalsapp.model.Animal
import com.android.mfcolak.animalsapp.service.AnimalDatabase
import com.android.mfcolak.animalsapp.util.NotificationHelper
import kotlinx.coroutines.launch

class AnimalDetailViewModel(application: Application): BaseViewModel(application){

    val animalLiveData = MutableLiveData<Animal>()

    fun checkRoomData(uuid: Int){
        launch {
            val dao = AnimalDatabase(getApplication()).animalDAO()
            val animal = dao.getAnimal(uuid)
            animalLiveData.value = animal

        }
    }
}